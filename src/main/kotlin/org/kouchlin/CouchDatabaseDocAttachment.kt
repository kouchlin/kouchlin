package org.kouchlin

import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.httpGet

class CouchDatabaseDocAttachment(val db: CouchDatabase, val doc: CouchDatabaseDocument, val name: String) {
	val attachmentURI = "${db.dbName}/${doc.id}/$name"

	fun exists(rev: String? = null, etag: String? = null): Triple<Int?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf()
		etag?.let { headers.put(IF_NONE_MATCH_HEADER, etag) }
		rev?.let { headers.put("If-Match", rev) }

		val (_, response, _) = attachmentURI.httpHead().header(headers).response()
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		val contentLenght: Int? = response.headers.get(CONTENT_LENGHT_HEADER)?.first()?.toInt()

		return Triple(contentLenght, responseEtag, transformStatusCode(response.statusCode))
	}

	fun get(rev: String? = null, etag: String? = null) {
		var headers: MutableMap<String, Any> = mutableMapOf()
		etag?.let { headers.put(IF_NONE_MATCH_HEADER, etag) }
		
		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		rev?.let({ parameters.add("rev" to rev) })
		
		val (_, response, result) = attachmentURI.httpDelete(parameters).header(headers).responseObject(gsonDeserializerOf<SaveResponse>())
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		//TODO Return content
		
	}

	fun delete(rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {
		var headers: MutableMap<String, Any> = mutableMapOf()
		fullCommit?.let { headers.put("X-Couch-Full-Commit", fullCommit) }

		var parameters: MutableList<Pair<String, Any?>> = mutableListOf()
		rev?.let({ parameters.add("rev" to rev) })
		batch?.let({ if (batch) parameters.add("batch" to "ok") })

		val (_, response, result) = attachmentURI.httpDelete(parameters).header(headers).responseObject(gsonDeserializerOf<SaveResponse>())
		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		return Triple(result.component1(), responseEtag, transformStatusCode(response.statusCode))
	}


}