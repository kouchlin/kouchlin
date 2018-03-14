package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.jackson.JacksonJsonAdapter

class JacksonJsonAdapterTest {
	
	@Test
	fun findDocumentIdTest() {
	  val adapter = JacksonJsonAdapter()
		
	  val testDoc = DummyJson("id1","rev1","value")
	
	  val (id,rev) = adapter.findDocumentId(testDoc)	
	  assert(id=="id1")
	  assert(rev=="rev1")
	}
}