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

class GsonCouchDBDocumentCRUDTest : GsonCouchDBBaseMockTest() {
    @Test
    fun putDocStringMockTest() = org.kouchlin.test.shared.putDocStringMockTest(couchdb)

    @Test
    fun postDocStringMockTest() = org.kouchlin.test.shared.postDocStringMockTest(couchdb)

    @Test
    fun putDocTestFromObjectMockTest() = org.kouchlin.test.shared.putDocTestFromObjectMockTest(couchdb, ::dummyJsonFactory)

    @Test
    fun postDocTestFromObjectMockTest() = org.kouchlin.test.shared.postDocTestFromObjectMockTest(couchdb, ::dummyJsonFactory)

    @Test
    fun allDocsParamsMockTest() = org.kouchlin.test.shared.allDocsParamsMockTest(couchdb)

    @Test
    fun allDocsMockTest() = org.kouchlin.test.shared.allDocsMockTest(couchdb)

    @Test
    fun allDocsWithDocMockTest() = org.kouchlin.test.shared.allDocsWithDocMockTest<DummyJson>(couchdb)

    @Test
    fun bulkDocsMockTest() = org.kouchlin.test.shared.bulkDocsMockTest(couchdb)
}