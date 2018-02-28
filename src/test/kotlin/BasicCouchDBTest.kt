import org.junit.Test
import org.kouchlin.CouchDB

class BasicCouchDBTest {

	@Test
	fun couchDBUpTest() {
		var couchdb = CouchDB("http://localhost:5984")
		assert(couchdb.up())
	}
	
	@Test
	fun couchDBVersionTest() {
		var couchdb = CouchDB("http://localhost:5984")
		assert(couchdb.version()=="2.0.0")
	}
	
	@Test
	fun couchDBAllDBsTest() {
		var couchdb = CouchDB("http://localhost:5984")
		val dbs = couchdb.databases()?.size
		if (dbs!=null) assert(dbs>0) else assert(false)
	}
}