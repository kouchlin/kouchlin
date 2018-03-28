package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.test.jackson.base.JacksonCouchDBBaseMockTest

class BasicCouchDBMockTest : JacksonCouchDBBaseMockTest() {

    @Test
    fun couchDBVersionTest() = org.kouchlin.test.shared.couchDBVersionTestMockedTest(couchdb)

    /*
    TODO: Complete mocked test with json serialization requirement
    @Test
    fun couchDBAllDBsTest() = org.kouchlin.test.shared.couchDBAllDBsTest(couchdb)

    @Test
    fun couchDBUpdatesTest() = org.kouchlin.test.shared.couchDBUpdatesTest(couchdb)

    @Test
    fun dbInfoTest() = org.kouchlin.test.shared.dbInfoTest(couchdb)

    @Test
    fun changesTest() = org.kouchlin.test.shared.changesTest(couchdb, ::dummyJsonFactory, ::rev)
    */
}