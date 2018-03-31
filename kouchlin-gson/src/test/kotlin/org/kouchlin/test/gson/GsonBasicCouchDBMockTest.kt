package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseMockTest

class BasicCouchDBMockTest : GsonCouchDBBaseMockTest() {

    @Test
    fun couchDBVersionMockTest() = org.kouchlin.test.shared.couchDBVersionTestMockedTest(couchdb)

    @Test
    fun couchDBAllDBsMockTest() = org.kouchlin.test.shared.couchDBAllDBsMockTest(couchdb)

    @Test
    fun couchDBUpdatesMockTest() = org.kouchlin.test.shared.couchDBUpdatesMockTest(couchdb)

    @Test
    fun dbInfoMockTest() = org.kouchlin.test.shared.dbInfoMockTest(couchdb)

    @Test
    fun changesTest() = org.kouchlin.test.shared.changesMockTest(couchdb)
}