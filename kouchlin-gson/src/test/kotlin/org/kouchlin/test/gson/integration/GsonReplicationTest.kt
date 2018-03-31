package org.kouchlin.test.gson.integration

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseTest

class GsonReplicationTest : GsonCouchDBBaseTest() {

    @Test
    fun replicationTest() = org.kouchlin.test.shared.replicationTest(couchdb)

    @Test
    fun replicationTestNotFound() = org.kouchlin.test.shared.replicationTestNotFound(couchdb)

}