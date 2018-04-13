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

fun existsDocTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val (_, _, status) = database.document("test1").exists()
    assertTrue(status == STATUS.NOT_FOUND)
}

fun getDocTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val (_, _, status) = database.document("test2")
            .get<String>(attachment = true, attsSince = listOf("1-aaa", "2-bbb", "3-ccc"))
    assertTrue(status == STATUS.NOT_FOUND)
}

fun putDocTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (_, _, status) = database.document("test").save(content = doc)
    assertTrue(status == STATUS.CREATED)

    val (size, etag, status2) = database.document("test").exists()
    assertTrue(status2 == STATUS.OK)
    assertTrue((size ?: 0) > 0)
    assertTrue(etag != null)
}

fun putDocTest2(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (_, _, status) = database.document().save(content = doc)

    assertTrue(status == STATUS.CREATED)
}

fun <T : Any> putDocTestFromObject(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = factory("test_with_id", null, "value")
    val (_, _, status) = database.document().save(content = doc)
    assertTrue(status == STATUS.CREATED)
}

fun <T : Any> postDocTestFromObject(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = factory(null, null, "value")
    val (_, _, status) = database.document().save(content = doc)
    assertTrue(status == STATUS.CREATED)
}

fun allDocsTest(couchdb: CouchDB) {
    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (_, _, status) = database.document("test-all-docs").save(content = doc)
    assertTrue(status == STATUS.CREATED)

    val (result, _) = database.allDocs<Any>()
    assertTrue(result?.rows!!.isNotEmpty())
}

inline fun <reified T : Any> allDocsTestObject(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-alldocs-db")
    database.create()

    val doc1 = factory("test_with_id1", null, "value")
    val doc2 = factory("test_with_id2", null, "value")
    val doc3 = factory("test_with_id3", null, "value")
    val docs = listOf(doc1, doc2, doc3)

    val (_, status) = database.bulkDocs(docs, true)
    assertTrue(status == STATUS.CREATED)

    val (result, status2) = database.allDocs<T>(includeDocs = true)
    assertTrue(status2 == STATUS.OK)
    assertTrue(result?.rows!!.isNotEmpty())
    assertTrue(result.rows.first().doc is T)
}

fun <T : Any> bulkDocsTest(couchdb: CouchDB, factory: (String?, String?, String) -> T) {
    val database = couchdb.database("kouchlin-test-db")

    val doc1 = factory("test_with_id1", null, "value")
    val doc2 = factory("test_with_id2", null, "value")
    val doc3 = factory("test_with_id3", null, "value")

    val docs = listOf(doc1, doc2, doc3)
    val (result, status) = database.bulkDocs(docs, true)
    assertTrue(status == STATUS.CREATED)
    assertTrue(result?.size == 3)
}