package org.kouchlin

import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.httpGet

enum class STATUS(val success: Boolean, val code: Int) {
	OK(true, 200),
	CREATED(true, 201),
	ACCEPTED(true, 202),
	NOT_MODIFIED(true, 304),
	UNAUTHORIZED(false, 401),
	NOT_FOUND(false, 404),
	BAD_REQUEST(true, 400),
	ERROR(false, 500),
	UNKNOWN(false, -1)
}

fun transformStatusCode(status: Int): STATUS {
	var response = STATUS.values().find { it.code == status }
	if (response == null) response = STATUS.UNKNOWN
	return response
}

class CouchDatabaseDocument(val db: CouchDatabase, val id: String? = null, val rev: String? = null) {

	val documentURI = "$db.dbName/$id"

	fun exists(etag: String? = null): Triple<Int?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf()
		etag?.let { headers.put("If-None-Match", etag) }

		val (_, response, _) = documentURI.httpHead().header(headers).response()
		val responseEtag = response.headers.get("ETag")?.first()
		val contentLenght: Int? = response.headers.get("Content-Length")?.first()?.toInt()

		return Triple(contentLenght, responseEtag, transformStatusCode(response.statusCode))
	}

	fun get(attachment: Boolean? = null, attEncodingInfo: Boolean? = null, attsSince: List<String>? = null,
			conflicts: Boolean? = null, deletedConflicts: Boolean? = null, latest: Boolean? = null,
			local_seq: Boolean? = null, meta: Boolean? = null, open_revs: List<String>? = null, rev: String? = null,
			revs: Boolean? = null, revsInfo: Boolean? = null, etag: String? = null): String {

		var headers: MutableMap<String, Any> = mutableMapOf()
		etag?.let { headers.put("If-None-Match", etag) }

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		//TODO: Process parameters
		
		val (_, response, _) = documentURI.httpGet().header(headers).response()
		return "TODO"
	}
}