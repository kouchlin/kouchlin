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