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

package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.jackson.JacksonJsonAdapter
import kotlin.test.assertTrue

class JacksonJsonAdapterTest {

    @Test
    fun findDocumentIdTest() {
        val adapter = JacksonJsonAdapter()

        val testDoc = DummyJson("id1", "rev1", "value")

        val (id, rev) = adapter.findDocumentIdRev(testDoc)
        assertTrue(id == "id1")
        assertTrue(rev == "rev1")
    }

    @Test
    fun findDocumentIdTest2() {
        val adapter = JacksonJsonAdapter()

        val testDoc = DummyJson(foo = "value")

        val (id, rev) = adapter.findDocumentIdRev(testDoc)
        assertTrue(id == null)
        assertTrue(rev == null)
    }

    @Test
    fun deleteDocumentIdTest() {
        val adapter = JacksonJsonAdapter()

        val testDoc = DummyJson("id1", "rev1", "value")

        val (id, rev, content) = adapter.deleteDocumentIdRev(testDoc)
        assertTrue(content != null)
        assertTrue(id == "id1")
        assertTrue(rev == "rev1")

        val deserialized = adapter.deserialize<DummyJson>(content!!, DummyJson::class.java)
        assertTrue(deserialized.id == null)
        assertTrue(deserialized.rev == null)
    }


    @Test
    fun deleteDocumentIdTest2() {
        val adapter = JacksonJsonAdapter()

        val testDoc = DummyJson(foo = "value")

        val (id, rev, content) = adapter.deleteDocumentIdRev(testDoc)
        assertTrue(content != null)
        assertTrue(id == null)
        assertTrue(rev == null)

        val deserialized = adapter.deserialize<DummyJson>(content!!, DummyJson::class.java)
        assertTrue(deserialized.id == null)
        assertTrue(deserialized.rev == null)
    }
}