package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.util.STATUS

/**
 * Created by JJ.Rodriguez on 26/03/2018.
 */
class GsonReplicationTest : GsonCouchDBBaseTest() {

    @Test
    fun replicationTest() {

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

    //@Test
    fun replicationTestNotFound() {

        val (response, status) = couchdb.replication().source("kouchlin-test-db")
                .target("kouchlin-target-db2")
                .trigger()

        //   assert(status == STATUS.NOT_FOUND)

    }

}