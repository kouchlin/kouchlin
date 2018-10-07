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
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import mu.KotlinLogging
import org.kouchlin.util.*

private val logger = KotlinLogging.logger {}

class CouchDatabaseDocument(val db: CouchDatabase, val id: String? = null, val rev: String? = null) {

    val documentURI = "${db.dbName}/${id.orEmpty()}"

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

        val (_, response, result) = when (T::class.java) {
            String::class.java -> request.responseString()
            else -> request.responseObject(CouchDB.adapter.deserialize(T::class.java))
        }

        val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)

        return Triple(result.component1() as T?, responseEtag, response.toStatus())
    }

    private fun saveWithPost(batch: Boolean? = null, content: Any): Triple<SaveResponse?, String?, STATUS> {
        val headers = configureHeaders(contentType = APPLICATION_JSON)
        val queryString = configureParameters(batch = batch)
                .encodeQueryString()
                .let { if (it.isNotEmpty()) "?$it" else it }
        val dbUriWithParams = "${db.dbName}$queryString"
        val (docId, docRev, jsonContent) = CouchDB.adapter.deleteDocumentIdRev(content)
        docId?.let { logger.warn("Doc rev $docId is ignored") }
        docRev?.let { logger.warn("Doc rev $docRev is ignored") }

        val (request, response, result) = dbUriWithParams.httpPost()
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

        if (docId != null && id != null && id != docId) {
            logger.warn("Document Id $docId included in the document is overridden by parameter $id")
        }

        val queryString = configureParameters(rev = rev ?: docRev,
                batch = batch,
                newEdits = newEdits)
                .encodeQueryString()
                .let { if (it.isNotEmpty()) "?$it" else it }

        val documentUriWithParams = "${db.dbName}/${(id ?: docId).orEmpty()}$queryString"
        val (request, response, result) = documentUriWithParams.httpPut()
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
        return if (id != null || docId != null) {
            saveWithPut(rev, batch, newEdits, content)
        } else {
            saveWithPost(batch, content)
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