/*
 * Copyright 2018 Kouchlin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kouchlin.test.shared

import org.kouchlin.CouchDB
import org.kouchlin.util.STATUS
import kotlin.test.assertTrue

fun couchDBUpTest(couchdb: CouchDB) {
    assertTrue(couchdb.up())
}

fun couchDBVersionTest(couchdb: CouchDB) {
    assertTrue(couchdb.version()!!.startsWith("2"))
}

fun couchDBAllDBsTest(couchdb: CouchDB) {
    val dbs = couchdb.databases()?.size
    assertTrue(dbs != null && dbs > 0)
}

fun couchDBUpdatesTest(couchdb: CouchDB) {
    val updates = couchdb.dbUpdates("now")
    assertTrue(updates!!.results!!.isEmpty())
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
    assertTrue(database.exists() == STATUS.OK)
}

fun notExistsDBTest(couchdb: CouchDB) {
    val database = couchdb.database("not-exists-db")
    assertTrue(database.exists() == STATUS.NOT_FOUND)
}

fun compactTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assertTrue(database.compact())

}

fun ensureFullCommitTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assertTrue(database.ensureFullCommit())

}

fun dbInfoTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    assertTrue(database.info().component1()?.dbName == "kouchlin-test-db")

}

fun createDBTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db-crud")
    database.delete()
    assertTrue(database.exists() == STATUS.NOT_FOUND)
    assertTrue(database.create() == STATUS.CREATED)
    assertTrue(database.exists() == STATUS.OK)
    assertTrue(database.delete() == STATUS.OK)
    assertTrue(database.exists() == STATUS.NOT_FOUND)
}

inline fun <reified T : Any> changesTest(couchdb: CouchDB, factory: (String?, String?, String) -> T, rev: (T?) -> String?) {
    val database = couchdb.database("kouchlin-changes-test-db")
    assertTrue(database.create() == STATUS.CREATED)

    val doc1 = factory("test_with_id1", null, "value")
    val doc2 = factory("test_with_id2", null, "value")
    val doc3 = factory("test_with_id3", null, "value")
    val docs = listOf(doc1, doc2, doc3)
    val (result, status) = database.bulkDocs(docs, true)
    assertTrue(status == STATUS.CREATED)
    assertTrue(result?.size == 3)

    val (changes, etag, status2) = database.changes<T>(includeDocs = true)
    assertTrue(status2 == STATUS.OK)
    assertTrue(etag != null)
    assertTrue(changes!!.results!!.size == 3)
    assertTrue(changes.results!!.first().doc is T)

    assertTrue(changes.lastSeq != null)

    val (_, _, status3) = database.changes<T>(etag = etag)
    assertTrue(status3 == STATUS.NOT_MODIFIED)

    val (dummyDoc, _, _) = database.document("test_with_id1").get<T>()
    database.document("test_with_id1").delete(rev(dummyDoc)!!)

    val (changes2, _, status4) = database.changes<T>(since = changes.lastSeq)
    assertTrue(status4 == STATUS.OK)
    assertTrue(changes2!!.results!!.size == 1)
    assertTrue(changes2.results!!.first().deleted!!)
}
