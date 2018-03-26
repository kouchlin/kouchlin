package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.util.STATUS

class JacksonReplicationTest : JacksonCouchDBBaseTest() {

    @Test
    fun replicationTest() {

        val (response, status) = couchdb.replication().source("kouchlin-test-db")
                .target("kouchlin-target-db")
                .continuous(true)
                .createTarget(true)
                .trigger()

        assert(status == STATUS.OK || status == STATUS.ACCEPTED)

        /*
        val (response2, status2) = couchdb.replication().source("kouchlin-test-db")
                .target("kouchlin-target-db")
                .continuous(true)
                .createTarget(true)
                .cancel(true)
                .trigger()
        assert(status == STATUS.OK || status == STATUS.ACCEPTED)
        */
    }

    //@Test
    fun replicationTestNotFound() {

        val (response, status) = couchdb.replication().source("kouchlin-test-db")
                .target("kouchlin-target-db2")
                .trigger()

        assert(status == STATUS.NOT_FOUND)

    }

}