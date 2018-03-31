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