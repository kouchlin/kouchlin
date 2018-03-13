package org.kouchlin

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import org.kouchlin.domain.DBInfo
import org.kouchlin.util.STATUS
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.toStatus
import org.kouchlin.util.configureParameters
import org.kouchlin.util.configureHeaders
import org.kouchlin.util.APPLICATION_JSON

internal const val COMPACT_ENDPOINT = "/_compact"
internal const val ENSURE_FULL_COMMIT_ENDPOINT = "/_ensure_full_commit"

class CouchDatabase(val server: CouchDB, val dbName: String) {

	internal var compact_uri = "$dbName$COMPACT_ENDPOINT";
	internal var ensureFullCommitUri = "$dbName$ENSURE_FULL_COMMIT_ENDPOINT"

	fun exists(): STATUS {
		val (_, response, _) = Fuel.head(dbName).configureAuthentication(server).response();
		return response.toStatus()
	}

	fun create(q: Int? = null): STATUS {
		val parameters = configureParameters(q = q)
		val (_, response, _) = dbName.httpPut(parameters).configureAuthentication(server).response();
		return response.toStatus()
	}

	fun delete(): STATUS {
		val (_, response, _) = dbName.httpDelete().configureAuthentication(server).response();
		return response.toStatus()
	}

	fun info(): Pair<DBInfo?, STATUS> {
		val (_, response, result) = dbName.httpGet()
				.configureAuthentication(server)
				.responseObject(CouchDB.adapter.deserializeDBInfo());
		
		return Pair(result.component1(), response.toStatus())
	}

	fun compact(ddoc: String? = null): Boolean {
		val ddoc_compact_uri = "$compact_uri${ddoc?.let({"/$ddoc"}).orEmpty()}"
		val headers = configureHeaders(contentType = APPLICATION_JSON)
		val (_, response, _) = ddoc_compact_uri.httpPost()
				.configureAuthentication(server)
				.header(headers).response()
		
		return response.statusCode == 202
	}

	fun ensureFullCommit(): Boolean {
		val headers = configureHeaders(contentType = APPLICATION_JSON)
		val (_, response, _) = ensureFullCommitUri.httpPost()
				.configureAuthentication(server)
				.header(headers).response();
		
		return response.statusCode == 201
	}

	fun document(id: String? = null, rev: String? = null) = CouchDatabaseDocument(this, id, rev)

}