import org.junit.Test
import org.kouchlin.util.STATUS

class CouchDBDocumentCRUDTest : CouchDBBaseTest() {
	@Test
	fun existsDocTest() {
		var database = couchdb.database("kouchlin-test-db")
		var (size, etag, status) = database.document("test1").exists()
		assert(status == STATUS.NOT_FOUND)
	}

	@Test
	fun getDocTest() {
		var database = couchdb.database("kouchlin-test-db")
		var (_, response, status) = database.document("test2").get(attachment=true, attsSince=listOf("1-aaa","2-bbb","3-ccc"))
		assert(status == STATUS.NOT_FOUND)
	}
	
	@Test
	fun putDocTest() {
		var database = couchdb.database("kouchlin-test-db")
		val doc ="""{
					"a":1
					}"""
		var (_, _, status) = database.document("test").save(content=doc)
		
		//assert(status == STATUS.NOT_FOUND)
	}
	
	@Test
	fun putDocTest2() {
		var database = couchdb.database("kouchlin-test-db")
		val doc ="""{
					"a":1
					}"""
		var (_, _, status) = database.document().save(content=doc)
		
		//assert(status == STATUS.NOT_FOUND)
	}

}