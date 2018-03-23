package org.kouchlin

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import mu.KotlinLogging
import org.kouchlin.auth.BasicAuthentication
import org.kouchlin.domain.DBUpdates
import org.kouchlin.util.Feed
import org.kouchlin.util.Version
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.JsonAdapter
import org.kouchlin.util.DummyJsonAdapter

private val logger = KotlinLogging.logger {}

internal const val UP_ENDPOINT = "_up";
internal const val VERSION_ENDPOINT = "";
internal const val ALLDBS_ENDPOINT = "_all_dbs";
internal const val DBUPDATES_ENDPOINT = "_db_updates";


class CouchDB(val serverURL: String, val authentication: BasicAuthentication? = null) {

	companion object {
		var adapter: JsonAdapter = DummyJsonAdapter()
	}

	init {
		FuelManager.instance.basePath = serverURL
	}

	fun up(): Boolean {
		val (_, response, _) = UP_ENDPOINT.httpGet().response()
		return (response.statusCode == 200)
	}

	fun version(): String? {
		val (_, _, result) = VERSION_ENDPOINT.httpGet().responseObject(adapter.deserialize(Version::class.java))
		return result.component1()?.version
	}

	@Suppress("UNCHECKED_CAST") 
	fun databases(): List<String>? {
		val (_, _, result) = ALLDBS_ENDPOINT.httpGet().responseObject(adapter.deserialize(List::class.java as Class<List<String>>))
		return result.component1()
	}

	fun dbUpdates(since: String? = null): DBUpdates? {

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()

		if (since != null) {
			parameters.add("since" to since)
		}

		val (request, _, result) = DBUPDATES_ENDPOINT.httpGet(parameters)
				.configureAuthentication(this)
				.responseObject(adapter.deserializeDBUpdates())

		logger.info(request.cUrlString())
		return result.component1()
	}

	fun dbUpdates(feed: Feed,
				  timeout: Int? = null,
				  heartbeat: Int? = null,
				  since: String? = null,
				  action: (DBUpdates?) -> Unit) {
		var parameters: MutableList<Pair<String, Any?>> = mutableListOf("feed" to feed.value)
		timeout?.let { parameters.add("timeout" to timeout) }
		heartbeat?.let { parameters.add("heartbeat" to heartbeat) }
		since?.let { parameters.add("since" to since) }

		DBUPDATES_ENDPOINT.httpGet(parameters)
				.configureAuthentication(this)
				.responseObject(adapter.deserializeDBUpdates()) { _, _, result -> result.fold(action, { err -> println(err) }) }
	}

	fun database(dbname: String) = CouchDatabase(this, dbname)

}