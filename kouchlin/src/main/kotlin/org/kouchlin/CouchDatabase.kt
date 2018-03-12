package org.kouchlin

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import org.kouchlin.domain.DBInfo
import org.kouchlin.util.STATUS
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.transformStatusCode

internal const val COMPACT_ENDPOINT = "/_compact"
internal const val ENSURE_FULL_COMMIT_ENDPOINT = "/_ensure_full_commit"

class CouchDatabase(val server: CouchDB, val dbName: String) {

	internal var compact_uri = "$dbName$COMPACT_ENDPOINT";
	internal var ensureFullCommitUri = "$dbName$ENSURE_FULL_COMMIT_ENDPOINT"

	fun exists(): STATUS {
		val (_, response, _) = Fuel.head(dbName).configureAuthentication(server).response();
		return transformStatusCode(response.statusCode)
	}

	fun create(q: Int? = null): STATUS {
		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		q?.let({ parameters.add("q" to q) })
		val (_, response, _) = dbName.httpPut(parameters).configureAuthentication(server).response();
		return transformStatusCode(response.statusCode)
	}

	fun delete(): STATUS {
		val (_, response, _) = dbName.httpDelete().configureAuthentication(server).response();
		return transformStatusCode(response.statusCode)
	}

	fun info(): Pair<DBInfo?, STATUS> {
		val (_, response, result) = dbName.httpGet().configureAuthentication(server).responseObject(CouchDB.deserializer.deserializeDBInfo());
		return Pair(result.component1(), transformStatusCode(response.statusCode))
	}

	fun compact(ddoc: String? = null): Boolean {
		val ddoc_compact_uri = if (ddoc == null) {
			compact_uri
		} else {
			"$compact_uri/$ddoc"
		}

		val (_, response, _) = ddoc_compact_uri.httpPost().configureAuthentication(server).header("Content-Type" to "application/json").response();
		return response.statusCode == 202
	}

	fun ensureFullCommit(): Boolean {
		val (_, response, _) = ensureFullCommitUri.httpPost().configureAuthentication(server).header("Content-Type" to "application/json").response();
		return response.statusCode == 201
	}

	fun document(id: String? = null, rev: String? = null) = CouchDatabaseDocument(this, id, rev)

}