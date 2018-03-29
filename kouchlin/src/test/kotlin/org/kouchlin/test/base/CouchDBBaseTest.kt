package org.kouchlin.test.base

import com.github.kittinunf.fuel.core.FuelManager
import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB

open class CouchDBBaseTest {
	companion object {
		lateinit var couchdb: CouchDB

		@BeforeClass
		@JvmStatic
		fun setUpClass() {
            FuelManager.instance
			couchdb = CouchDB("http://localhost:5984")
			couchdb.database("kouchlin-test-db").create()
		}

		@AfterClass
		@JvmStatic
		fun teardown() {
			couchdb.database("kouchlin-test-db").delete()
		}

	}
}