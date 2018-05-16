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

package org.kouchlin.test.gson.integration

import org.junit.Test
import org.junit.experimental.categories.Category
import org.kouchlin.test.base.RequireCouchDB
import org.kouchlin.test.gson.base.GsonCouchDBBaseTest
import org.kouchlin.test.gson.dummyJsonFactory
import org.kouchlin.test.gson.rev

@Category(RequireCouchDB::class)
class GsonBasicCouchDBTest : GsonCouchDBBaseTest() {
    @Test
    fun couchDBUpTest() = org.kouchlin.test.shared.couchDBUpTest(couchdb)

    @Test
    fun couchDBVersionTest() = org.kouchlin.test.shared.couchDBVersionTest(couchdb)

    @Test
    fun couchDBAllDBsTest() = org.kouchlin.test.shared.couchDBAllDBsTest(couchdb)

    @Test
    fun couchDBUpdatesTest() = org.kouchlin.test.shared.couchDBUpdatesTest(couchdb)

    @Test
    fun existsDBTest() = org.kouchlin.test.shared.existsDBTest(couchdb)

    @Test
    fun notExistsDBTest() = org.kouchlin.test.shared.notExistsDBTest(couchdb)

    @Test
    fun compactTest() = org.kouchlin.test.shared.compactTest(couchdb)

    @Test
    fun ensureFullCommitTest() = org.kouchlin.test.shared.ensureFullCommitTest(couchdb)

    @Test
    fun dbInfoTest() = org.kouchlin.test.shared.dbInfoTest(couchdb)

    @Test
    fun createDBTest() = org.kouchlin.test.shared.createDBTest(couchdb)

    @Test
    fun changesTest() = org.kouchlin.test.shared.changesTest(couchdb, ::dummyJsonFactory, ::rev)
}