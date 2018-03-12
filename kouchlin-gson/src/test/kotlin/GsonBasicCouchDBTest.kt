import org.junit.Test
import org.kouchlin.util.STATUS

class GsonBasicCouchDBTest : GsonCouchDBBaseTest() {

	@Test
	fun couchDBUpTest() {
		assert(couchdb.up())
	}

	@Test
	fun couchDBVersionTest() {
		assert(couchdb.version()!!.startsWith("2"))
	}

	@Test
	fun couchDBAllDBsTest() {
		val dbs = couchdb.databases()?.size
		assert(dbs != null && dbs > 0)
	}

	@Test
	fun couchDBUpdatesTest() {
		var updates = couchdb.dbUpdates()
		updates = couchdb.dbUpdates("now")
		assert(updates!!.results.size == 0);
	}

//	@Test
//	fun couchDBUpdatesLongpollTest() {
//		couchdb.dbUpdates(feed = Feed.LONGPOLL, since = "now", action = { updates -> println(">>>> " + updates) })
////		Thread.sleep(10000)
//		runBlocking {
//			delay(100000)
//		}
//		println("finish")
//	}

	@Test
	fun existsDBTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.exists() == STATUS.OK)
	}

	@Test
	fun notExistsDBTest() {
		var database = couchdb.database("not-exists-db")
		assert(database.exists() == STATUS.NOT_FOUND)
	}

	@Test
	fun compactTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.compact())

	}

	@Test
	fun ensureFullCommitTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.ensureFullCommit())

	}

	@Test
	fun dbInfoTest() {
		var database = couchdb.database("kouchlin-test-db")
		assert(database.info().component1()?.dbName == "kouchlin-test-db")

	}

	@Test
	fun createDBTest() {
		var database = couchdb.database("kouchlin-test-db-crud")
		database.delete()
		assert(database.exists() == STATUS.NOT_FOUND)
		assert(database.create() == STATUS.CREATED)
		assert(database.exists() == STATUS.OK)
		assert(database.delete() == STATUS.OK)
		assert(database.exists() == STATUS.NOT_FOUND)
	}

}