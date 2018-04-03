package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseMockTest

class GsonCouchDBDocumentCRUDTest : GsonCouchDBBaseMockTest() {
    @Test
    fun putDocStringMockTest() = org.kouchlin.test.shared.putDocStringMockTest(couchdb)

    @Test
    fun postDocStringMockTest() = org.kouchlin.test.shared.postDocStringMockTest(couchdb)

    @Test
    fun putDocTestFromObjectMockTest() = org.kouchlin.test.shared.putDocTestFromObjectMockTest(couchdb, ::dummyJsonFactory)

    @Test
    fun postDocTestFromObjectMockTest() = org.kouchlin.test.shared.postDocTestFromObjectMockTest(couchdb, ::dummyJsonFactory)

    @Test
    fun allDocsParamsMockTest() = org.kouchlin.test.shared.allDocsParamsMockTest(couchdb)

    @Test
    fun allDocsMockTest() = org.kouchlin.test.shared.allDocsMockTest(couchdb)

    @Test
    fun allDocsWithDocMockTest() = org.kouchlin.test.shared.allDocsWithDocMockTest<DummyJson>(couchdb)

    @Test
    fun bulkDocsMockTest() = org.kouchlin.test.shared.bulkDocsMockTest(couchdb)
}