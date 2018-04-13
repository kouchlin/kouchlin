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

package org.kouchlin.util

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.HttpException
import com.github.kittinunf.fuel.httpPost
import mu.KotlinLogging
import org.kouchlin.CouchDB
import org.kouchlin.domain.ReplicationResponse

internal const val REPLICATION_ENDPOINT = "/_replicate";

private val logger = KotlinLogging.logger {}

class CouchDBReplication(val server: CouchDB) {
    val replicatorUri = "${server.serverURL}$REPLICATION_ENDPOINT"
    var cancel: Boolean? = null
        private set
    var continuous: Boolean? = null
        private set
    var createTarget: Boolean? = null
        private set
    var docIds: List<String>? = null
        private set
    var filter: String? = null
        private set
    var proxy: String? = null
        private set
    var source: String? = null
        private set
    var target: String? = null
        private set

    fun cancel(cancel: Boolean): CouchDBReplication = apply { this.cancel = cancel }
    fun continuous(continuous: Boolean): CouchDBReplication = apply { this.continuous = continuous }
    fun createTarget(createTarget: Boolean): CouchDBReplication = apply { this.createTarget = createTarget }
    fun docIds(docIds: List<String>): CouchDBReplication = apply { this.docIds = docIds }
    fun filter(filter: String): CouchDBReplication = apply { this.filter = filter }
    fun proxy(proxy: String): CouchDBReplication = apply { this.proxy = proxy }
    fun source(source: String): CouchDBReplication = apply { this.source = source }
    fun target(target: String): CouchDBReplication = apply { this.target = target }

    fun trigger(): Pair<ReplicationResponse<*>?, STATUS> {
        try {
            val headers = configureHeaders(contentType = APPLICATION_JSON)

            val requestContent = CouchDB.adapter.serializeReplicationRequest(cancel,
                    continuous,
                    createTarget,
                    docIds,
                    filter,
                    proxy,
                    source,
                    target)

            val (request, response, result) = replicatorUri.httpPost()
                    .configureAuthentication(server)
                    .header(headers)
                    .body(requestContent)
                    .responseObject(CouchDB.adapter.deserializeReplicationResponse())

            logger.info { request.cUrlString() }
            return Pair(result.get(), response.toStatus())
        } catch (e: FuelError) {
            return Pair(null, e.response.toStatus())
        }

    }
}