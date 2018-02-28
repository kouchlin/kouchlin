package org.kouchlin

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpDelete

class CouchDatabase(val dbName: String) {

	fun exists(): Boolean {
		val (_, response, _) = Fuel.head(dbName).response();
		return (response.statusCode == 200)
	}

	fun create(q: Int? = null): Boolean {
		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()

		if (q != null) {
			parameters.add("q" to q)
		}
		val (_, response, _) = Fuel.put(dbName, parameters).response();
		return (response.statusCode == 201 || response.statusCode == 202)
	}

	fun delete(): Boolean {
		val (_, response, _) = dbName.httpDelete().response();
		return (response.statusCode == 200 || response.statusCode == 202)
	}

}