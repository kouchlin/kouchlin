package org.kouchlin

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpPost

internal const val COMPACT_ENDPOINT = "/_compact"
internal const val ENSURE_FULL_COMMIT_ENDPOINT = "/_ensure_full_commit"

class CouchDatabase(val dbName: String) {

	internal var compact_uri = "$dbName$COMPACT_ENDPOINT";
	internal var ensureFullCommitUri = "$dbName$ENSURE_FULL_COMMIT_ENDPOINT"

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

	fun compact(ddoc: String? = null): Boolean {
		val ddoc_compact_uri = if (ddoc == null) {
			compact_uri
		} else {
			"$compact_uri/$ddoc"
		}

		val (_, response, _) = ddoc_compact_uri.httpPost().header("Content-Type" to "application/json").response();
		return response.statusCode == 202
	}

	fun ensureFullCommit(): Boolean {
		val (_, response, _) = ensureFullCommitUri.httpPost().header("Content-Type" to "application/json").response();
		return response.statusCode == 201
	}

}