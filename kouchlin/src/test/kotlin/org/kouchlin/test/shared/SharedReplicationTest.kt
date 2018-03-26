package org.kouchlin.test.shared

import org.junit.Test
import org.kouchlin.CouchDB
import org.kouchlin.util.STATUS

fun replicationTest(couchdb: CouchDB) {

    val (response, status) = couchdb.replication().source("kouchlin-test-db")
            .target("kouchlin-target-db")
            .continuous(true)
            .createTarget(true)
            .trigger()

    assert(status == STATUS.OK || status == STATUS.ACCEPTED)

    /*   val (response2, status2) = couchdb.replication().source("kouchlin-test-db")
               .target("kouchlin-target-db")
               .continuous(true)
               .createTarget(true)
               .cancel(true)
               .trigger()

    assert(status == STATUS.OK || status == STATUS.ACCEPTED) */
}

fun replicationTestNotFound(couchdb: CouchDB) {

    val (response, status) = couchdb.replication().source("kouchlin-test-db")
            .target("kouchlin-target-db2")
            .trigger()

    //   assert(status == STATUS.NOT_FOUND)

}
