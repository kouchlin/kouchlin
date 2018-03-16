package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.util.STATUS

class GsonCouchDBDocumentCRUDTest : GsonCouchDBBaseTest() {
	@Test
	fun existsDocTest() {
		var database = couchdb.database("kouchlin-test-db")
		var (size, etag, status) = database.document("test1").exists()
		assert(status == STATUS.NOT_FOUND)
	}

	@Test
	fun getDocTest() {
		var database = couchdb.database("kouchlin-test-db")
		var (_, _, status) = database.document("test2").get<String>(attachment = true, attsSince = listOf("1-aaa", "2-bbb", "3-ccc"))
		assert(status == STATUS.NOT_FOUND)
	}

	@Test
	fun putDocTest() {
		var database = couchdb.database("kouchlin-test-db")
		val doc = """{
					"a":1
					}"""
		var (_, _, status) = database.document("test").save(content = doc)
		assert(status == STATUS.CREATED)

		var (size, etag, status2) = database.document("test").exists()
		assert(status2 == STATUS.OK)
		assert((size ?: 0) > 0) 
		assert(etag != null)
	}

	@Test
	fun putDocTest2() {
		var database = couchdb.database("kouchlin-test-db")
		val doc = """{
					"a":1
					}"""
		var (_, _, status) = database.document().save(content = doc)

		assert(status == STATUS.CREATED)
	}

	@Test
	fun allDocsTest() {
		var database = couchdb.database("kouchlin-test-db")
		val doc = """{
					"a":1
					}"""
		var (_, _, status) = database.document("test-all-docs").save(content = doc)
		assert(status == STATUS.CREATED)

		val (result,etag,status2) = database.allDocs()
		assert(result?.rows!!.size>0)
	}
	
}