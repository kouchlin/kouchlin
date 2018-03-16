package org.kouchlin.util

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import org.kouchlin.CouchDB
import java.lang.UnsupportedOperationException

const val IF_NONE_MATCH_HEADER = "If-None-Match"
const val IF_MATCH_HEADER = "If-Match"
const val FULL_COMMIT_HEADER = "X-Couch-Full-Commit"
const val CONTENT_LENGHT_HEADER = "Content-Length"
const val CONTENT_TYPE_HEADER = "Content-Type"
const val ETAG_HEADER = "ETag"

const val APPLICATION_JSON = "application/json"

enum class STATUS(val success: Boolean, val code: Int) {
	OK(true, 200),
	CREATED(true, 201),
	ACCEPTED(true, 202),
	NOT_MODIFIED(true, 304),
	UNAUTHORIZED(false, 401),
	NOT_FOUND(false, 404),
	BAD_REQUEST(true, 400),
	CONFLICT(false, 409),
	PRECONDITION_FAILED(false, 412),
	ERROR(false, 500),
	UNKNOWN(false, -1)
}

enum class Feed(val value: String) {
	NORMAL("normal"),
	LONGPOLL("longpoll"),
	CONTINOUS("continous"),
	EVENTSOURCE("eventsource")
}


fun configureHeaders(rev: String? = null,
					 etag: String? = null,
					 fullCommit: Boolean? = null,
					 contentType: String? = null): Map<String, Any> {

	var headers: MutableMap<String, Any> = mutableMapOf()
	etag?.let { headers.put(IF_NONE_MATCH_HEADER, etag) }
	rev?.let { headers.put(IF_MATCH_HEADER, rev) }
	fullCommit?.let { headers.put(FULL_COMMIT_HEADER, fullCommit) }
	contentType?.let { headers.put(CONTENT_TYPE_HEADER, contentType) }

	return headers;
}

fun configureParameters(attachment: Boolean? = null,
						attEncodingInfo: Boolean? = null,
						attsSince: List<String>? = null,
						conflicts: Boolean? = null,
						deletedConflicts: Boolean? = null,
						latest: Boolean? = null,
						localSeq: Boolean? = null,
						meta: Boolean? = null,
						openRevs: List<String>? = null,
						rev: String? = null,
						revs: Boolean? = null,
						revsInfo: Boolean? = null,
						batch: Boolean? = null,
						newEdits: Boolean? = null,
						q: Int? = null): List<Pair<String, Any?>> {

	var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
	attachment?.let({ parameters.add("attachment" to attachment) })
	attEncodingInfo?.let({ parameters.add("att_encoding_info" to attEncodingInfo) })
	attsSince?.let({ parameters.add("atts_since" to attsSince.joinToString(prefix = "[", postfix = "]", separator = ",", transform = { "\"$it\"" })) })
	conflicts?.let({ parameters.add("conflicts" to conflicts) })
	deletedConflicts?.let({ parameters.add("deleted_conflicts" to deletedConflicts) })
	latest?.let({ parameters.add("latest" to latest) })
	localSeq?.let({ parameters.add("local_seq" to localSeq) })
	meta?.let({ parameters.add("meta" to meta) })
	openRevs?.let({ parameters.add("open_revs" to openRevs.joinToString(prefix = "[", postfix = "]", separator = ",", transform = { "\"$it\"" })) })
	rev?.let({ parameters.add("rev" to rev) })
	revs?.let({ parameters.add("revs" to revs) })
	revsInfo?.let({ parameters.add("revs_info" to revsInfo) })
	batch?.let({ if (batch) parameters.add("batch" to "ok") })
	newEdits?.let({ parameters.add("new_edits" to newEdits) })
	q?.let({ parameters.add("q" to q) })
	return parameters;
}


fun configureViewParameters(conflicts: Boolean? = null,
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
					  updateSeq: Boolean? = null): List<Pair<String, Any?>> {

	var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
	descending?.let({ parameters.add("descending" to descending) })
	attachments?.let({ parameters.add("attachments" to attachments) })
	attEncodingInfo?.let({ parameters.add("att_encoding_info" to attEncodingInfo) })
	conflicts?.let({ parameters.add("conflicts" to conflicts) })
	endKey?.let({ parameters.add("endKey" to endKey) })
	endKeyDocId?.let({ parameters.add("endkey_docid" to endKeyDocId) })
	group?.let({ parameters.add("group" to group) })
	groupLevel?.let({ parameters.add("group_level" to groupLevel) })
	includeDocs?.let({ parameters.add("include_docs" to includeDocs) })
	inclusiveEnd?.let({ parameters.add("inclusive_end" to inclusiveEnd) })
	key?.let({ parameters.add("key" to key) })
	keys?.let({ parameters.add("keys" to keys.joinToString(prefix = "[", postfix = "]", separator = ",", transform = { "\"$it\"" })) })
	limit?.let({parameters.add("limit" to limit) })
	reduce?.let({ parameters.add("reduce" to reduce) })
	skip?.let({ parameters.add("skip" to skip) })
	sorted?.let({ parameters.add("sorted" to sorted) })
	stable?.let({ parameters.add("stable" to stable) })
	stale?.let({ parameters.add("stale" to stale) })
	startKey?.let({ parameters.add("start_key" to startKey) })
	startKeyDocId?.let({ parameters.add("startkey_docid" to startKeyDocId) })
	update?.let({ parameters.add("update" to update) })
	updateSeq?.let({ parameters.add("update_seq" to updateSeq) })
	
	return parameters;
}

fun configureAuthentication(server: CouchDB, request: Request): Request {
	if (server.authentication != null) {
		return request.authenticate(server.authentication.username, server.authentication.password)
	} else {
		return request
	}
}

fun Request.configureAuthentication(server: CouchDB): Request = configureAuthentication(server, this)

inline fun <reified T> Response.getHeaderValue(name: String): T {
	val value = this.headers.get(name)?.first()
	return when (T::class) {
		String::class -> value as T
		Int::class -> value?.toInt() as T
		else -> throw UnsupportedOperationException("${T::class} conversion is not supported")
	}
}

fun Response.toStatus(): STATUS {
	val response = STATUS.values().find { it.code == this.statusCode }
	return response ?: STATUS.UNKNOWN
}


