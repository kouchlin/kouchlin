package org.kouchlin

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpGet
import org.kouchlin.auth.BasicAuthentication
import org.kouchlin.domain.DBUpdates


internal const val UP_ENDPOINT = "_up";
internal const val VERSION_ENDPOINT = "";
internal const val ALLDBS_ENDPOINT = "_all_dbs";
internal const val DBUPDATES_ENDPOINT = "_db_updates";

data class Version(val version: String)
enum class Feed(val value: String) {NORMAL("normal"), LONGPOLL("longpoll"), CONTINOUS("continous"), EVENTSOURCE("eventsource") }

class CouchDB(val serverURL: String, val authentication: BasicAuthentication? = null) {

	init {
		FuelManager.instance.basePath = serverURL

	}

	fun up(): Boolean {
		val (_, response, _) = UP_ENDPOINT.httpGet().response()
		return (response.statusCode == 200)
	}

	fun version(): String? {
		val (_, _, result) = VERSION_ENDPOINT.httpGet().responseObject(gsonDeserializerOf<Version>())
		return result.component1()?.version
	}

	fun databases(): List<String>? {
		val (_, _, result) = ALLDBS_ENDPOINT.httpGet().responseObject(gsonDeserializerOf<List<String>>())
		return result.component1()
	}

	fun dbUpdates(since: String? = null): DBUpdates? {

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()

		if (since != null) {
			parameters.add("since" to since)
		}

		val (_, _, result) = Fuel.get(DBUPDATES_ENDPOINT, parameters).responseObject(gsonDeserializerOf<DBUpdates>())
		return result.component1()
	}

	fun dbUpdates(feed: Feed, timeout: Int? = null, heartbeat: Int? = null, since: String? = null, action: (updates: DBUpdates?) -> Unit) {
		var parameters: MutableList<Pair<String, Any?>> = mutableListOf("feed" to feed.value)
		timeout?.let {parameters.add("timeout" to timeout)}
		heartbeat?.let {parameters.add("heartbeat" to heartbeat)}
		since?.let {parameters.add("since" to since)}

		Fuel.get(DBUPDATES_ENDPOINT, parameters).responseObject(gsonDeserializerOf<DBUpdates>()) { _, _, result -> result.fold(action, { err -> println(err) }) }
	}

	fun database(dbname: String) = CouchDatabase(dbname)


}