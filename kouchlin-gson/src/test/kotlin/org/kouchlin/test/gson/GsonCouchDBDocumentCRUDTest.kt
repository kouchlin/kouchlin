package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseTest

class GsonCouchDBDocumentCRUDTest : GsonCouchDBBaseTest() {
    @Test
    fun existsDocTest() = org.kouchlin.test.shared.existsDocTest(couchdb)

    @Test
    fun getDocTest() = org.kouchlin.test.shared.getDocTest(couchdb)

    @Test
    fun putDocTest() = org.kouchlin.test.shared.putDocTest(couchdb)

    @Test
    fun putDocTest2() = org.kouchlin.test.shared.putDocTest2(couchdb)

    @Test
    fun putDocTestFromObject() = org.kouchlin.test.shared.putDocTestFromObject(couchdb, ::dummyJsonFactory)

    @Test
    fun postDocTestFromObject() = org.kouchlin.test.shared.postDocTestFromObject(couchdb, ::dummyJsonFactory)

    @Test
    fun allDocsTest() = org.kouchlin.test.shared.allDocsTest(couchdb)

    @Test
    fun allDocsTestObject() = org.kouchlin.test.shared.allDocsTestObject(couchdb, ::dummyJsonFactory)

    @Test
    fun bulkDocsTest() = org.kouchlin.test.shared.bulkDocsTest(couchdb, ::dummyJsonFactory)
}