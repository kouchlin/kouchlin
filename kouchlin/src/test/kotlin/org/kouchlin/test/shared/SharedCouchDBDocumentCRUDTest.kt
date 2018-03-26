package org.kouchlin.test.shared

import org.junit.Test
import org.kouchlin.CouchDB
import org.kouchlin.util.STATUS

fun existsDocTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val (size, etag, status) = database.document("test1").exists()
    assert(status == STATUS.NOT_FOUND)
}

fun getDocTest(couchdb: CouchDB) {
    var database = couchdb.database("kouchlin-test-db")
    var (_, _, status) = database.document("test2")
            .get<String>(attachment = true, attsSince = listOf("1-aaa", "2-bbb", "3-ccc"))
    assert(status == STATUS.NOT_FOUND)
}

fun putDocTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (_, _, status) = database.document("test").save(content = doc)
    assert(status == STATUS.CREATED)

    val (size, etag, status2) = database.document("test").exists()
    assert(status2 == STATUS.OK)
    assert((size ?: 0) > 0)
    assert(etag != null)
}

fun putDocTest2(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (_, _, status) = database.document().save(content = doc)

    assert(status == STATUS.CREATED)
}

fun <T : Any> putDocTestFromObject(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = factory("test_with_id", null, "value")
    val (_, _, status) = database.document().save(content = doc)
    assert(status == STATUS.CREATED)
}

fun <T : Any> postDocTestFromObject(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = factory(null, null, "value")
    val (_, _, status) = database.document().save(content = doc)
    assert(status == STATUS.CREATED)
}

fun allDocsTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (_, _, status) = database.document("test-all-docs").save(content = doc)
    assert(status == STATUS.CREATED)

    val (result, status2) = database.allDocs<Any>()
    assert(result?.rows!!.isNotEmpty())
}

inline fun <reified T : Any> allDocsTestObject(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-alldocs-db")
    database.create()

    val doc1 = factory("test_with_id1", null, "value")
    val doc2 = factory("test_with_id2", null, "value")
    val doc3 = factory("test_with_id3", null, "value")
    val docs = listOf(doc1, doc2, doc3)

    val (_, status) = database.bulkDocs(docs, true)
    assert(status == STATUS.CREATED)

    val (result, status2) = database.allDocs<T>(includeDocs = true)
    assert(status2 == STATUS.OK)
    assert(result?.rows!!.isNotEmpty())
    assert(result.rows.first().doc is T)
}

fun <T : Any> bulkDocsTest(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-db")

    val doc1 = factory("test_with_id1", null, "value")
    val doc2 = factory("test_with_id2", null, "value")
    val doc3 = factory("test_with_id3", null, "value")

    val docs = listOf(doc1, doc2, doc3)
    val (result, status) = database.bulkDocs(docs, true)
    assert(status == STATUS.CREATED)
    assert(result?.size == 3)
}