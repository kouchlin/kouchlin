package org.kouchlin.test.jackson.base

import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB
import org.kouchlin.jackson.JacksonJsonAdapter

open class JacksonCouchDBBaseMockTest {
	companion object {
		lateinit var couchdb: CouchDB

		@BeforeClass
		@JvmStatic
		fun setUpClass() {
			CouchDB.adapter = JacksonJsonAdapter()
			couchdb = CouchDB("http://localhost:5984")
		}

		@AfterClass
		@JvmStatic
		fun teardown() {
		}

	}
}