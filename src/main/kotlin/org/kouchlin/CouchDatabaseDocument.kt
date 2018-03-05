package org.kouchlin

import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

internal const val ETAG_HEADER = "ETag"
internal const val IF_NONE_MATCH_HEADER = "If-None-Match"
internal const val CONTENT_LENGHT_HEADER = "Content-Length"
internal const val CONTENT_TYPE_HEADER = "Content-Type"

enum class STATUS(val success: Boolean, val code: Int) {
	OK(true, 200),
	CREATED(true, 201),
	ACCEPTED(true, 202),
	NOT_MODIFIED(true, 304),
	UNAUTHORIZED(false, 401),
	NOT_FOUND(false, 404),
	BAD_REQUEST(true, 400),
	CONFLICT(false, 409),
	ERROR(false, 500),
	UNKNOWN(false, -1)
}

data class SaveResponse(val id: String, val ok: Boolean, val rev: String)

fun transformStatusCode(status: Int): STATUS {
	var response = STATUS.values().find { it.code == status }
	if (response == null) response = STATUS.UNKNOWN
	return response
}

class CouchDatabaseDocument(val db: CouchDatabase, val id: String? = null, val rev: String? = null) {

	val documentURI = "${db.dbName}/${id.orEmpty()}"

	fun exists(etag: String? = null): Triple<Int?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf()
		etag?.let { headers.put(IF_NONE_MATCH_HEADER, etag) }

		val (_, response, _) = documentURI.httpHead().header(headers).response()
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		val contentLenght: Int? = response.headers.get(CONTENT_LENGHT_HEADER)?.first()?.toInt()

		return Triple(contentLenght, responseEtag, transformStatusCode(response.statusCode))
	}

	fun get(attachment: Boolean? = null, attEncodingInfo: Boolean? = null, attsSince: List<String>? = null,
			conflicts: Boolean? = null, deletedConflicts: Boolean? = null, latest: Boolean? = null,
			localSeq: Boolean? = null, meta: Boolean? = null, openRevs: List<String>? = null, rev: String? = null,
			revs: Boolean? = null, revsInfo: Boolean? = null, etag: String? = null): Triple<String?, String?, STATUS> {

		var headers: MutableMap<String, Any> = mutableMapOf()
		etag?.let { headers.put(ETAG_HEADER, etag) }

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

		val (request, response, result) = documentURI.httpGet(parameters).header(headers).responseString()
		logger.info(request.cUrlString())
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()

		return Triple(result.component1(), responseEtag, transformStatusCode(response.statusCode))
	}

	internal fun saveWithPost(batch: Boolean? = null, content: String): Triple<SaveResponse?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf(CONTENT_TYPE_HEADER to "application/json")

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		batch?.let({ if (batch) parameters.add("batch" to "ok") })

		val (request, response, result) = documentURI.httpPost(parameters).header(headers).body(content).responseObject(gsonDeserializerOf<SaveResponse>())
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		logger.info(request.cUrlString())
		return Triple(result.component1(), responseEtag, transformStatusCode(response.statusCode))
	}

	internal fun saveWithPut(rev: String? = null, batch: Boolean? = null, newEdits: Boolean? = null, content: String): Triple<SaveResponse?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf(CONTENT_TYPE_HEADER to "application/json")

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		rev?.let({ parameters.add("rev" to rev) })
		batch?.let({ if (batch) parameters.add("batch" to "ok") })
		newEdits?.let({ parameters.add("new_edits" to newEdits) })

		val (request, response, result) = documentURI.httpPut(parameters).header(headers).body(content).responseObject(gsonDeserializerOf<SaveResponse>())
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		logger.info(request.cUrlString())
		return Triple(result.component1(), responseEtag, transformStatusCode(response.statusCode))
	}

	fun save(rev: String? = null, batch: Boolean? = null, newEdits: Boolean? = null, content: String): Triple<SaveResponse?, String?, STATUS> {
		if (id != null) {
			return saveWithPut(rev, batch, newEdits, content)
		} else {
			return saveWithPost(batch, content)
		}
	}

	fun delete(rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf()
		fullCommit?.let { headers.put("X-Couch-Full-Commit", fullCommit) }

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		rev?.let({ parameters.add("rev" to rev) })
		batch?.let({ if (batch) parameters.add("batch" to "ok") })

		val (_, response, result) = documentURI.httpDelete(parameters).header(headers).responseObject(gsonDeserializerOf<SaveResponse>())
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		return Triple(result.component1(), responseEtag, transformStatusCode(response.statusCode))
	}

// Copy is a non-standard method in HTTP that is not supported by FUEL library
//	fun copy(destination: String, rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {	
//		Fuel.request("COPY",)
//	}

	fun attachment(name: String) = CouchDatabaseDocAttachment(db, this, name)
}