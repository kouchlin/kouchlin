package org.kouchlin

import org.kouchlin.auth.BasicAuthentication
import uy.klutter.core.uri.UriBuilder
import uy.klutter.core.uri.buildUri
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.gson.gsonDeserializerOf


const val UP_ENDPOINT = "_up";
const val VERSION_ENDPOINT = "";
const val ALLDBS_ENDPOINT = "_all_dbs";

data class Version(val version: String)

class CouchDB(val serverURL: String, val authentication: BasicAuthentication? = null) {

	init {
		FuelManager.instance.basePath = serverURL
	}

	fun up(): Boolean {
		val (_, response, result) = UP_ENDPOINT.httpGet().response()
		println(result.toString())
		return (response.statusCode == 200)
	}

	fun version(): String? {
		val (_, _, result) = VERSION_ENDPOINT.httpGet().responseObject(gsonDeserializerOf<Version>())
		println(result.toString())
		return result.component1()?.version
	}

	fun databases(): List<String>? {
		val (_, _, result) = ALLDBS_ENDPOINT.httpGet().responseObject(gsonDeserializerOf<List<String>>())
		return result.component1()
	}
}