package org.kouchlin

import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.Fuel
import org.kouchlin.util.transformStatusCode
import org.kouchlin.util.configureHeaders
import org.kouchlin.util.configureParameters
import org.kouchlin.util.SaveResponse
import org.kouchlin.util.STATUS
import org.kouchlin.util.configureAuthentication

class CouchDatabaseDocAttachment(val db: CouchDatabase, val doc: CouchDatabaseDocument, val name: String) {

	val attachmentURI = "${db.dbName}/${doc.id}/$name"

	fun exists(rev: String? = null, etag: String? = null): Triple<Int?, String?, STATUS> {

		val headers = configureHeaders(etag = etag, rev = rev)

		val (_, response, _) = attachmentURI.httpHead()
				.configureAuthentication(db.server)
				.header(headers)
				.response()

		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		val contentLenght: Int? = response.headers.get(CONTENT_LENGHT_HEADER)?.first()?.toInt()

		return Triple(contentLenght, responseEtag, transformStatusCode(response.statusCode))
	}

	fun get(rev: String? = null, etag: String? = null) {

		val headers = configureHeaders(etag = etag)
		val parameters = configureParameters(rev = rev)


		val (_, response, result) = attachmentURI.httpDelete(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.responseObject(gsonDeserializerOf<SaveResponse>())

		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		//TODO: Return content

	}

	fun delete(rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {

		val headers = configureHeaders(fullCommit = fullCommit)
		val parameters = configureParameters(rev = rev, batch = batch)

		val (_, response, result) = attachmentURI.httpDelete(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.responseObject(gsonDeserializerOf<SaveResponse>())

		val responseEtag = response.headers.get(ETAG_HEADER)?.first()
		return Triple(result.component1(), responseEtag, transformStatusCode(response.statusCode))
	}

}