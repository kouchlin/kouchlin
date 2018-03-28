package org.kouchlin.test.jackson.integration

import org.junit.Test
import org.kouchlin.test.jackson.base.JacksonCouchDBBaseTest

class JacksonReplicationTest : JacksonCouchDBBaseTest() {

    @Test
    fun replicationTest() = org.kouchlin.test.shared.replicationTest(couchdb)

    //@Test
    fun replicationTestNotFound() = org.kouchlin.test.shared.replicationTestNotFound(couchdb)

}