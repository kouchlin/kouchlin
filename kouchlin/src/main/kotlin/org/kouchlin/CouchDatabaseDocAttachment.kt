package org.kouchlin

import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpHead
import org.kouchlin.util.CONTENT_LENGHT_HEADER
import org.kouchlin.util.ETAG_HEADER
import org.kouchlin.util.STATUS
import org.kouchlin.util.SaveResponse
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.configureHeaders
import org.kouchlin.util.configureParameters
import org.kouchlin.util.getHeaderValue
import org.kouchlin.util.toStatus

class CouchDatabaseDocAttachment(val db: CouchDatabase, val doc: CouchDatabaseDocument, val name: String) {

	val attachmentURI = "${db.dbName}/${doc.id}/$name"

	fun exists(rev: String? = null, etag: String? = null): Triple<Int?, String?, STATUS> {

		val headers = configureHeaders(etag = etag, rev = rev)

		val (_, response, _) = attachmentURI.httpHead()
				.configureAuthentication(db.server)
				.header(headers)
				.response()

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
		val contentLenght = response.getHeaderValue<Int?>(CONTENT_LENGHT_HEADER)

		return Triple(contentLenght, responseEtag, response.toStatus())
	}

	fun get(rev: String? = null, etag: String? = null) {

		val headers = configureHeaders(etag = etag)
		val parameters = configureParameters(rev = rev)


		val (_, response, result) = attachmentURI.httpDelete(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
		//TODO: Return content

	}

	fun delete(rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {

		val headers = configureHeaders(fullCommit = fullCommit)
		val parameters = configureParameters(rev = rev, batch = batch)

		val (_, response, result) = attachmentURI.httpDelete(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
		return Triple(result.component1(), responseEtag, response.toStatus())
	}

}