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

package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseMockTest

class BasicCouchDBMockTest : GsonCouchDBBaseMockTest() {

    @Test
    fun couchDBVersionMockTest() = org.kouchlin.test.shared.couchDBVersionTestMockedTest(couchdb)

    @Test
    fun couchDBAllDBsMockTest() = org.kouchlin.test.shared.couchDBAllDBsMockTest(couchdb)

    @Test
    fun couchDBUpdatesMockTest() = org.kouchlin.test.shared.couchDBUpdatesMockTest(couchdb)

    @Test
    fun dbInfoMockTest() = org.kouchlin.test.shared.dbInfoMockTest(couchdb)

    @Test
    fun changesMockTest() = org.kouchlin.test.shared.changesMockTest(couchdb)

    @Test
    fun replicationMock() = org.kouchlin.test.shared.replicationMockTest(couchdb)
}