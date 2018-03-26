package org.kouchlin.util

import com.github.kittinunf.fuel.httpPost
import mu.KotlinLogging
import org.kouchlin.CouchDB
import org.kouchlin.domain.HistoryEntry
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
        return Pair(result.get() , response.toStatus())
    }
}