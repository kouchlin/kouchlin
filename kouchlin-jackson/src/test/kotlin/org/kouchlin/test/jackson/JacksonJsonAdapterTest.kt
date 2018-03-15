package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.jackson.JacksonJsonAdapter

class JacksonJsonAdapterTest {

	@Test
	fun findDocumentIdTest() {
		val adapter = JacksonJsonAdapter()

		val testDoc = DummyJson("id1", "rev1", "value")

		val (id, rev) = adapter.findDocumentIdRev(testDoc)
		assert(id == "id1")
		assert(rev == "rev1")
	}

//	@Test
//	fun deleteDocumentIdTest() {
//		val adapter = JacksonJsonAdapter()
//
//		val testDoc = DummyJson("id1", "rev1", "value")
//
//		val (id, rev, content) = adapter.deleteDocumentIdRev(testDoc)
//		assert(content != null)
//		assert(id == "id1")
//		assert(rev == "rev1")
//
//		val deserialized = adapter.deserialize<DummyJson>(content!!, DummyJson::class.java)
//		assert(deserialized.id == null)
//		assert(deserialized.rev == null)
//	}
}