package org.kouchlin.test.jackson

import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB
import org.kouchlin.jackson.JacksonJsonAdapter

open class JacksonCouchDBBaseTest {
	companion object {
		lateinit var couchdb: CouchDB

		@BeforeClass
		@JvmStatic
		fun setUpClass() {
			CouchDB.adapter = JacksonJsonAdapter()
			couchdb = CouchDB("http://localhost:5984")
			couchdb.database("kouchlin-test-db").create()
		}

		@AfterClass
		@JvmStatic
		fun teardown() {
			couchdb.database("kouchlin-test-db").delete()
			couchdb.database("kouchlin-changes-test-db").delete()
		}

	}
}