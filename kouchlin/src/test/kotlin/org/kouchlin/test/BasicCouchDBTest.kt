package org.kouchlin.test

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.kouchlin.util.Feed
import org.kouchlin.util.STATUS

class BasicCouchDBTest : CouchDBBaseTest() {

	@Test
	fun couchDBUpTest() {
		assert(couchdb.up())
	}

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
	fun createDBTest() {
		var database = couchdb.database("kouchlin-test-db-crud")
		database.delete()
		assert(database.exists() == STATUS.NOT_FOUND)
		assert(database.create() == STATUS.CREATED)
		assert(database.exists() == STATUS.OK)
		assert(database.delete() == STATUS.OK)
		assert(database.exists() == STATUS.NOT_FOUND)
	}

}