import org.junit.AfterClass
import org.junit.BeforeClass
import org.kouchlin.CouchDB
import org.kouchlin.gson.GsonDeserializerHelper

open class GsonCouchDBBaseTest {
	companion object {
		lateinit var couchdb: CouchDB

		@BeforeClass
		@JvmStatic
		fun setUpClass() {
			CouchDB.deserializer = GsonDeserializerHelper()
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