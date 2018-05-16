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

package org.kouchlin.test.jackson.integration

import org.junit.Test
import org.junit.experimental.categories.Category
import org.kouchlin.test.base.RequireCouchDB
import org.kouchlin.test.jackson.base.JacksonCouchDBBaseTest
import org.kouchlin.test.jackson.dummyJsonFactory

@Category(RequireCouchDB::class)
class JacksonCouchDBDocumentCRUDTest : JacksonCouchDBBaseTest() {
    @Test
    fun existsDocTest() = org.kouchlin.test.shared.existsDocTest(couchdb)

    @Test
    fun getDocTest() = org.kouchlin.test.shared.getDocTest(couchdb)

    @Test
    fun putDocTest() = org.kouchlin.test.shared.putDocTest(couchdb)

    @Test
    fun putDocTest2() = org.kouchlin.test.shared.putDocTest2(couchdb)

    @Test
    fun putDocTestFromObject() = org.kouchlin.test.shared.putDocTestFromObject(couchdb, ::dummyJsonFactory)

    @Test
    fun postDocTestFromObject() = org.kouchlin.test.shared.postDocTestFromObject(couchdb, ::dummyJsonFactory)

    @Test
    fun allDocsTest() = org.kouchlin.test.shared.allDocsTest(couchdb)

    @Test
    fun allDocsTestObject() = org.kouchlin.test.shared.allDocsTestObject(couchdb, ::dummyJsonFactory)

    @Test
    fun bulkDocsTest() = org.kouchlin.test.shared.bulkDocsTest(couchdb, ::dummyJsonFactory)

}