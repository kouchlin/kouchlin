package org.kouchlin.test.shared

import org.junit.Test
import org.kouchlin.CouchDB
import org.kouchlin.util.STATUS
import kotlin.test.assertTrue

fun couchDBUpTest(couchdb: CouchDB) {
    assert(couchdb.up())
}

fun couchDBVersionTest(couchdb: CouchDB) {
    assert(couchdb.version()!!.startsWith("2"))
}

fun couchDBAllDBsTest(couchdb: CouchDB) {
    val dbs = couchdb.databases()?.size
    assert(dbs != null && dbs > 0)
}

fun couchDBUpdatesTest(couchdb: CouchDB) {
    val updates = couchdb.dbUpdates("now")
    assert(updates!!.results!!.size == 0);
}

//	@Test
//	fun couchDBUpdatesLongpollTest() {
//		couchdb.dbUpdates(feed = Feed.LONGPOLL, since = "now", action = { updates -> println(">>>> " + updates) })
////		Thread.sleep(10000)
//		runBlocking {
//			delay(100000)
//		}
//		println("finish")
//	}

fun existsDBTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assert(database.exists() == STATUS.OK)
}

fun notExistsDBTest(couchdb: CouchDB) {
    val database = couchdb.database("not-exists-db")
    assert(database.exists() == STATUS.NOT_FOUND)
}

fun compactTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assert(database.compact())

}

fun ensureFullCommitTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assert(database.ensureFullCommit())

}

fun dbInfoTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assert(database.info().component1()?.dbName == "kouchlin-test-db")

}

fun createDBTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db-crud")
    database.delete()
    assert(database.exists() == STATUS.NOT_FOUND)
    assert(database.create() == STATUS.CREATED)
    assert(database.exists() == STATUS.OK)
    assert(database.delete() == STATUS.OK)
    assert(database.exists() == STATUS.NOT_FOUND)
}

inline fun <reified T : Any> changesTest(couchdb: CouchDB, factory: (String?, String?, String) -> T, rev: (T?) -> String?) {
    val database = couchdb.database("kouchlin-changes-test-db")
    assert(database.create() == STATUS.CREATED)

    val doc1 = factory("test_with_id1", null, "value")
    val doc2 = factory("test_with_id2", null, "value")
    val doc3 = factory("test_with_id3", null, "value")
    val docs = listOf(doc1, doc2, doc3)
    val (result, status) = database.bulkDocs(docs, true)
    assert(status == STATUS.CREATED)
    assert(result?.size == 3)

    val (changes, etag, status2) = database.changes<T>(includeDocs = true)
    assert(status2 == STATUS.OK)
    assert(etag != null)
    assert(changes!!.results!!.size == 3)
    assert(changes.results!!.first().doc is T)

    assert(changes.lastSeq != null)

    val (_, _, status3) = database.changes<T>(etag = etag)
    assert(status3 == STATUS.NOT_MODIFIED)

    val (dummyDoc, _, _) = database.document("test_with_id1").get<T>()
    database.document("test_with_id1").delete(rev(dummyDoc)!!)

    val (changes2, _, status4) = database.changes<T>(since = changes.lastSeq)
    assert(status4 == STATUS.OK)
    assert(changes2!!.results!!.size == 1)
    assert(changes2.results!!.first().deleted!!)
}
