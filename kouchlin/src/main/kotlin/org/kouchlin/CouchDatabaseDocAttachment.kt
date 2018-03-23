package org.kouchlin

import com.github.kittinunf.fuel.core.Blob
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.fuel.httpUpload
import mu.KotlinLogging
import org.kouchlin.domain.Attachment
import org.kouchlin.util.CONTENT_LENGHT_HEADER
import org.kouchlin.util.CONTENT_TYPE_HEADER
import org.kouchlin.util.ETAG_HEADER
import org.kouchlin.util.STATUS
import org.kouchlin.util.SaveResponse
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.configureHeaders
import org.kouchlin.util.configureParameters
import org.kouchlin.util.getHeaderValue
import org.kouchlin.util.toStatus
import java.io.InputStream
import java.io.StringBufferInputStream

private val logger = KotlinLogging.logger {}

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

    fun get(rev: String? = null, etag: String? = null): Triple<Attachment?, String?, STATUS> {

        val headers = configureHeaders(etag = etag)
        val parameters = configureParameters(rev = rev)

        val (_, response, _) = attachmentURI.httpGet(parameters)
                .configureAuthentication(db.server)
                .header(headers)
                .response()

        val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
        val responseContent = response.getHeaderValue<String?>(CONTENT_TYPE_HEADER)

        val responseStatus = response.toStatus()

        if (responseStatus == STATUS.OK) {
            return Triple(Attachment(response.data, responseContent!!), responseEtag, response.toStatus())
        } else {
            return Triple(null, null, response.toStatus())
        }
    }

    fun save(data: ByteArray, contentType: String, rev: String? = null): Triple<SaveResponse?, String?, STATUS> {
        val headers = configureHeaders(contentType = contentType)
        val parameters = configureParameters(rev = rev)
        val (_, response, result) = attachmentURI.httpPut(parameters)
                .configureAuthentication(db.server)
                .header(headers)
                .body(data)
                .responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))

        val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
        return Triple(result.get(), responseEtag, response.toStatus())
    }

    fun save(data: String, contentType: String, rev: String? = null): Triple<SaveResponse?, String?, STATUS> {
        val headers = configureHeaders(contentType = contentType, rev = rev)

        val (request, response, result) = attachmentURI.httpPut()
                .configureAuthentication(db.server)
                .header(headers)
                .body(data)
                .responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))
        logger.info(request.cUrlString())
        val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
        return Triple(result.get(), responseEtag, response.toStatus())
    }

    fun delete(rev: String? = null, batch: Boolean? = null, fullCommit: Boolean? = null): Triple<SaveResponse?, String?, STATUS> {

        val headers = configureHeaders(fullCommit = fullCommit)
        val parameters = configureParameters(rev = rev, batch = batch)

        val (request, response, result) = attachmentURI.httpDelete(parameters)
                .configureAuthentication(db.server)
                .header(headers)
                .responseObject(CouchDB.adapter.deserialize(SaveResponse::class.java))
        logger.info(request.cUrlString())
        val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
        return Triple(result.get(), responseEtag, response.toStatus())
    }

}