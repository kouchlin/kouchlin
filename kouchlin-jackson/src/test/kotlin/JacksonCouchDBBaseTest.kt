import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB
import org.kouchlin.jackson.JacksonDeserializerHelper

open class JacksonCouchDBBaseTest {
	companion object {
		lateinit var couchdb: CouchDB

		@BeforeClass
		@JvmStatic
		fun setUpClass() {
			CouchDB.deserializer = JacksonDeserializerHelper()
			couchdb = CouchDB("http://localhost:5984")
			couchdb.database("kouchlin-test-db").create()
		}

		@AfterClass
		@JvmStatic
		fun teardown() {
			couchdb.database("kouchlin-test-db").delete()
		}

	}
}