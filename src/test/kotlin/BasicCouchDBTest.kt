import org.junit.Test
import org.kouchlin.CouchDB
import org.kouchlin.Feed
import org.junit.BeforeClass

class BasicCouchDBTest : CouchDBBaseTest() {

	@Test
	fun couchDBUpTest() {
		assert(couchdb.up())
	}

	@Test
	fun couchDBVersionTest() {
		assert(couchdb.version() == "2.0.0")
	}

	@Test
	fun couchDBAllDBsTest() {
		val dbs = couchdb.databases()?.size
		assert(dbs != null && dbs > 0) 
	}

	@Test
	fun couchDBUpdatesTest() {
		var updates = couchdb.dbUpdates()
		updates = couchdb.dbUpdates(updates?.lastSeq)
		assert(updates!!.results.size == 0);
	}

	@Test
	fun couchDBUpdatesLongpollTest() {
		couchdb.dbUpdates(feed = Feed.LONGPOLL, since = "now", action = { updates -> println(">>>> " + updates) })
		Thread.sleep(10000)
		println("finish")
	}

	@Test
	fun existsDBTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.exists())
	}

	@Test
	fun notExistsDBTest() {
		var database = couchdb.database("not-exists-db")
		assert(!database.exists())

	}

	@Test
	fun createDBTest() {
		var database = couchdb.database("kouchlin-test-db-crud")
		assert(!database.exists())
		assert(database.create())
		assert(database.exists())
		assert(database.delete())
		assert(!database.exists())
	}
}