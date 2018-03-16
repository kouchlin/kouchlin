package org.kouchlin

import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import mu.KotlinLogging
import org.kouchlin.util.APPLICATION_JSON
import org.kouchlin.util.CONTENT_LENGHT_HEADER
import org.kouchlin.util.ETAG_HEADER
import org.kouchlin.util.STATUS
import org.kouchlin.util.SaveResponse
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.configureHeaders
import org.kouchlin.util.configureParameters
import org.kouchlin.util.getHeaderValue
import org.kouchlin.util.toStatus

private val logger = KotlinLogging.logger {}

class CouchDatabaseDocument(val db: CouchDatabase, val id: String? = null, val rev: String? = null) {

	private val documentURI = "${db.dbName}/${id.orEmpty()}"

	fun exists(etag: String? = null): Triple<Int?, String?, STATUS> {
		val headers = configureHeaders(etag = etag)

		val (_, response, _) = documentURI.httpHead()
				.configureAuthentication(db.server)
				.header(headers)
				.response()

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
		val contentLenght = response.getHeaderValue<Int?>(CONTENT_LENGHT_HEADER)

		return Triple(contentLenght, responseEtag, response.toStatus())
	}

	inline fun <reified T : Any> get(attachment: Boolean? = null,
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
									 etag: String? = null): Triple<T?, String?, STATUS> {

		val headers = configureHeaders(etag = etag)

		val parameters = configureParameters(attachment = attachment,
				attEncodingInfo = attEncodingInfo,
				attsSince = attsSince,
				conflicts = conflicts,
				deletedConflicts = deletedConflicts,
				latest = latest,
				localSeq = localSeq,
				meta = meta,
				openRevs = openRevs,
				rev = rev,
				revs = revs,
				revsInfo = revsInfo)

		val request = documentURI.httpGet(parameters)
				.configureAuthentication(db.server)
				.header(headers)

		val (_, response, result) = if (String::class.java is T)
			request.responseString()
		else
			request.responseObject(CouchDB.adapter.deserialize(T::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)

		return Triple(result.component1() as T?, responseEtag, response.toStatus())
	}

	private fun saveWithPost(batch: Boolean? = null, content: Any): Triple<SaveResponse?, String?, STATUS> {
		val headers = configureHeaders(contentType = APPLICATION_JSON)
		val parameters = configureParameters(batch = batch)

		val (docId, docRev, jsonContent) = CouchDB.adapter.deleteDocumentIdRev(content)
		docId?.let{logger.warn("Doc rev $docId is ignored")}
		docRev?.let{logger.warn("Doc rev $docRev is ignored")}

		val (request, response, result) = db.dbName.httpPost(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.body(jsonContent!!)
				.responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
		logger.info(request.cUrlString())
		return Triple(result.component1(), responseEtag, response.toStatus())
	}

	private fun saveWithPut(rev: String? = null, batch: Boolean? = null, newEdits: Boolean? = null, content: Any): Triple<SaveResponse?, String?, STATUS> {
		val headers = configureHeaders(contentType = APPLICATION_JSON)

		val (docId, docRev, jsonContent) = CouchDB.adapter.deleteDocumentIdRev(content)

		if (docId == null && id == null) {
			throw IllegalArgumentException("Document id should be provided")
		}

		if (docId != null && id !=null && id != docId) {
			logger.warn("Document Id $docId included in the document is overriden by parameter $id")
		}

		val parameters = configureParameters(rev = rev ?: docRev,
				batch = batch,
				newEdits = newEdits)


		val (request, response, result) = "${db.dbName}/${(id ?: docId).orEmpty()}".httpPut(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.body(jsonContent!!)
				.responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
		logger.info(request.cUrlString())
		return Triple(result.component1(), responseEtag, response.toStatus())
	}

	fun save(rev: String? = null, batch: Boolean? = null, newEdits: Boolean? = null, content: Any): Triple<SaveResponse?, String?, STATUS> {
		val (docId, _, _) = CouchDB.adapter.findDocumentIdRev(content)
		if (id != null || docId != null) {
			return saveWithPut(rev, batch, newEdits, content)
		} else {
			assert(rev == null)
			return saveWithPost(batch, content)
		}
	}

	fun delete(rev: String, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {
		val headers = configureHeaders(fullCommit = fullCommit)
		val parameters = configureParameters(rev = rev, batch = batch)

		val (_, response, result) = documentURI.httpDelete(parameters)
				.configureAuthentication(db.server)
				.header(headers)
				.responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)

		return Triple(result.component1(), responseEtag, response.toStatus())
	}

// Copy is a non-standard method in HTTP that is not supported by FUEL library
//	fun copy(destination: String, rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {	
//		Fuel.request("COPY",)
//	}

	fun attachment(name: String) = CouchDatabaseDocAttachment(db, this, name)
}