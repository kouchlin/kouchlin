package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.util.STATUS

class GsonBasicCouchDBTest : GsonCouchDBBaseTest() {

	@Test
	fun couchDBUpTest() {
		assert(couchdb.up())
	}

	@Test
	fun couchDBVersionTest() {
		assert(couchdb.version()!!.startsWith("2"))
	}

	@Test
	fun couchDBAllDBsTest() {
		val dbs = couchdb.databases()?.size
		assert(dbs != null && dbs > 0)
	}

	@Test
	fun couchDBUpdatesTest() {
		var updates = couchdb.dbUpdates("now")
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

	@Test
	fun existsDBTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.exists() == STATUS.OK)
	}

	@Test
	fun notExistsDBTest() {
		var database = couchdb.database("not-exists-db")
		assert(database.exists() == STATUS.NOT_FOUND)
	}

	@Test
	fun compactTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.compact())

	}

	@Test
	fun ensureFullCommitTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.ensureFullCommit())

	}

	@Test
	fun dbInfoTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.info().component1()?.dbName == "kouchlin-test-db")

	}

	@Test
	fun createDBTest() {
		var database = couchdb.database("kouchlin-test-db-crud")
		database.delete()
		assert(database.exists() == STATUS.NOT_FOUND)
		assert(database.create() == STATUS.CREATED)
		assert(database.exists() == STATUS.OK)
		assert(database.delete() == STATUS.OK)
		assert(database.exists() == STATUS.NOT_FOUND)
	}

	@Test
	fun changesTest() {
		var database = couchdb.database("kouchlin-changes-test-db")
		assert(database.create() == STATUS.CREATED)

		val doc1 = DummyJson(id = "test_with_id1", foo = "value")
		val doc2 = DummyJson(id = "test_with_id2", foo = "value")
		val doc3 = DummyJson(id = "test_with_id3", foo = "value")
		val docs = listOf(doc1, doc2, doc3)
		val (result, status) = database.bulkDocs(docs, true)
		assert(status == STATUS.CREATED)
		assert(result?.size == 3)

		val (changes, etag, status2) = database.changes<DummyJson>(includeDocs = true)
		assert(status2 == STATUS.OK)
		assert(etag != null)
		assert(changes!!.results!!.size == 3)
		assert(changes.results!!.first().doc is DummyJson)

		assert(changes.lastSeq != null)

		val (_, _, status3) = database.changes<DummyJson>(etag = etag)
		assert(status3 == STATUS.NOT_MODIFIED)

		val (dummyDoc, _, _) = database.document("test_with_id1").get<DummyJson>()
		database.document("test_with_id1").delete(dummyDoc!!.rev!!)
		
		val (changes2, _, status4) = database.changes<DummyJson>(since = changes.lastSeq)
		assert(status4 == STATUS.OK)
		assert(changes2!!.results!!.size == 1)
		assert(changes2.results!!.first().deleted!!)
	}

}