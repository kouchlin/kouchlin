package org.kouchlin.test.shared

import org.kouchlin.CouchDB
import org.kouchlin.util.STATUS
import kotlin.test.assertTrue

fun replicationTest(couchdb: CouchDB) {

    val (response, status) = couchdb.replication().source("http://127.0.0.1:5984/kouchlin-test-db")
            .target("http://127.0.0.1:5984/kouchlin-target-db")
            .createTarget(true)
            .trigger()

    assertTrue(status.success, "A successful response expected but $status found")

}

fun replicationTestNotFound(couchdb: CouchDB) {

    val (response, status) = couchdb.replication().source("http://127.0.0.1:5984/kouchlin-test-db")
            .target("http://127.0.0.1:5984/kouchlin-target-db2")
            .trigger()

    assertTrue(status == STATUS.ERROR || status == STATUS.NOT_FOUND)

}
