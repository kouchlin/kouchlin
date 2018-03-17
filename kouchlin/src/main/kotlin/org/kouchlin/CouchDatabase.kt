package org.kouchlin

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import org.kouchlin.domain.DBInfo
import org.kouchlin.domain.ViewResult
import org.kouchlin.domain.ViewResultRow
import org.kouchlin.domain.ViewRevRow
import org.kouchlin.util.APPLICATION_JSON
import org.kouchlin.util.STATUS
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.configureHeaders
import org.kouchlin.util.configureParameters
import org.kouchlin.util.toStatus
import org.kouchlin.domain.BulkDocsResult
import org.kouchlin.domain.BulkDocs

internal const val COMPACT_ENDPOINT = "/_compact"
internal const val ENSURE_FULL_COMMIT_ENDPOINT = "/_ensure_full_commit"
internal const val BULK_DOCS_ENDPOINT = "/_bulk_docs"

class CouchDatabase(val server: CouchDB, val dbName: String) {

	internal var compact_uri = "$dbName$COMPACT_ENDPOINT";
	internal var ensureFullCommitUri = "$dbName$ENSURE_FULL_COMMIT_ENDPOINT"
	internal var bulkDocsUri = "$dbName$BULK_DOCS_ENDPOINT"

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
		val ddoc_compact_uri = "$compact_uri${ddoc?.let({ "/$ddoc" }).orEmpty()}"
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

	fun <T> allDocs(conflicts: Boolean? = null,
				descending: Boolean? = null,
				endKey: String? = null,
				endKeyDocId: String? = null,
				group: Boolean? = null,
				groupLevel: Int? = null,
				includeDocs: Boolean? = null,
				attachments: Boolean? = null,
				attEncodingInfo: Boolean? = null,
				inclusiveEnd: Boolean? = null,
				key: String? = null,
				keys: List<String>? = null,
				limit: Int? = null,
				reduce: Boolean? = null,
				skip: Int? = null,
				sorted: Boolean? = null,
				stable: Boolean? = null,
				stale: String? = null, /* ok,update_after,false*/
				startKey: String? = null,
				startKeyDocId: String? = null,
				update: String? = null, /*true,false,lazy*/
				updateSeq: Boolean? = null
	): Triple<ViewResult<ViewResultRow<ViewRevRow,T>>?, String?, STATUS?> {

		return CouchDatabaseView(this, "_all_docs").get<ViewRevRow,T, ViewResult<ViewResultRow<ViewRevRow,T>>>(conflicts = conflicts,
				descending = descending,
				endKey = endKey,
				endKeyDocId = endKeyDocId,
				group = group,
				groupLevel = groupLevel,
				includeDocs = includeDocs,
				attachments = attachments,
				attEncodingInfo = attEncodingInfo,
				inclusiveEnd = inclusiveEnd,
				key = key,
				keys = keys,
				limit = limit,
				reduce = reduce,
				skip = skip,
				sorted = sorted,
				stable = stable,
				stale = stale,
				startKey = startKey,
				startKeyDocId = startKeyDocId,
				update = update,
				updateSeq = updateSeq)

	}

	fun <T> designDocs(conflicts: Boolean? = null,
				   descending: Boolean? = null,
				   endKey: String? = null,
				   endKeyDocId: String? = null,
				   group: Boolean? = null,
				   groupLevel: Int? = null,
				   includeDocs: Boolean? = null,
				   attachments: Boolean? = null,
				   attEncodingInfo: Boolean? = null,
				   inclusiveEnd: Boolean? = null,
				   key: String? = null,
				   keys: List<String>? = null,
				   limit: Int? = null,
				   reduce: Boolean? = null,
				   skip: Int? = null,
				   sorted: Boolean? = null,
				   stable: Boolean? = null,
				   stale: String? = null, /* ok,update_after,false*/
				   startKey: String? = null,
				   startKeyDocId: String? = null,
				   update: String? = null, /*true,false,lazy*/
				   updateSeq: Boolean? = null
	): Triple<ViewResult<ViewResultRow<ViewRevRow,T>>?, String?, STATUS?> {

		return CouchDatabaseView(this, "_design_docs").get<ViewRevRow,T, ViewResult<ViewResultRow<ViewRevRow,T>>>(conflicts = conflicts,
				descending = descending,
				endKey = endKey,
				endKeyDocId = endKeyDocId,
				group = group,
				groupLevel = groupLevel,
				includeDocs = includeDocs,
				attachments = attachments,
				attEncodingInfo = attEncodingInfo,
				inclusiveEnd = inclusiveEnd,
				key = key,
				keys = keys,
				limit = limit,
				reduce = reduce,
				skip = skip,
				sorted = sorted,
				stable = stable,
				stale = stale,
				startKey = startKey,
				startKeyDocId = startKeyDocId,
				update = update,
				updateSeq = updateSeq)

	}

	fun <T : Any> bulkDocs(docs: List<T>, newEdits: Boolean? = null): Pair<List<BulkDocsResult>?, STATUS> {
		val headers = configureHeaders(contentType = APPLICATION_JSON)
		val jsonContent = CouchDB.adapter.serializeBulkDocs<T>(docs, newEdits)

		val (_, response, result) = bulkDocsUri.httpPost()
				.configureAuthentication(server)
				.header(headers)
				.body(jsonContent)
				.responseObject(CouchDB.adapter.deserialize<List<BulkDocsResult>>(List::class.java as Class<List<BulkDocsResult>>))
		return Pair(result.component1(), response.toStatus())
	}

	fun document(id: String? = null, rev: String? = null) = CouchDatabaseDocument(this, id, rev)

}