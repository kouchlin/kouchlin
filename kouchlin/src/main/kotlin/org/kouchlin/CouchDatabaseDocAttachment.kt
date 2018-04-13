/*
 * Copyright 2018 Kouchlin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kouchlin

import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpHead
import com.github.kittinunf.fuel.httpPut
import mu.KotlinLogging
import org.kouchlin.domain.Attachment
import org.kouchlin.util.*

private val logger = KotlinLogging.logger {}

class CouchDatabaseDocAttachment(val db: CouchDatabase, val doc: CouchDatabaseDocument, val name: String) {

    private val attachmentURI = "${db.dbName}/${doc.id}/$name"

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

        val (request, response, _) = attachmentURI.httpGet(parameters)
                .configureAuthentication(db.server)
                .header(headers)
                .response()
        logger.info(request.cUrlString())
        val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)
        val responseContent = response.getHeaderValue<String?>(CONTENT_TYPE_HEADER)

        val responseStatus = response.toStatus()

        return when (responseStatus) {
            STATUS.OK -> Triple(Attachment(response.data, responseContent!!), responseEtag, response.toStatus())
            else -> Triple(null, null, response.toStatus())
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
        val headers = configureHeaders(contentType = contentType)
        val queryString = configureParameters(rev = rev).encodeQueryString()
        val attachmentUriWithParams = "$attachmentURI?$queryString"
        val (request, response, result) = attachmentUriWithParams.httpPut()
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