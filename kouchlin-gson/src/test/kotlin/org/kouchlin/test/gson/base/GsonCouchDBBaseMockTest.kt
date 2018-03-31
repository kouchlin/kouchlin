package org.kouchlin.test.gson.base

import com.github.kittinunf.fuel.core.FuelManager
import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB
import org.kouchlin.gson.GsonJsonAdapter

open class GsonCouchDBBaseMockTest {
    companion object {
        lateinit var couchdb: CouchDB

        @BeforeClass
        @JvmStatic
        fun setUpClass() {
            CouchDB.adapter = GsonJsonAdapter()
            couchdb = CouchDB("http://localhost:5984")
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            FuelManager.instance = FuelManager()
        }

    }
}