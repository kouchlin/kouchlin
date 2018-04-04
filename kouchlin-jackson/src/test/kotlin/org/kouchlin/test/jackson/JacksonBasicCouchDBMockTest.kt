package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.test.jackson.base.JacksonCouchDBBaseMockTest

class BasicCouchDBMockTest : JacksonCouchDBBaseMockTest() {

    @Test
    fun couchDBVersionMockTest() = org.kouchlin.test.shared.couchDBVersionTestMockedTest(couchdb)

    @Test
    fun couchDBAllDBsMockTest() = org.kouchlin.test.shared.couchDBAllDBsMockTest(couchdb)

    @Test
    fun couchDBUpdatesMockTest() = org.kouchlin.test.shared.couchDBUpdatesMockTest(couchdb)

    @Test
    fun dbInfoMockTest() = org.kouchlin.test.shared.dbInfoMockTest(couchdb)

    @Test
    fun changesMockTest() = org.kouchlin.test.shared.changesMockTest(couchdb)

    @Test
    fun replicationMock() = org.kouchlin.test.shared.replicationMockTest(couchdb)
}