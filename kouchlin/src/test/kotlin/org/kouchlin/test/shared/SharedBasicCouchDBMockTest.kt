package org.kouchlin.test.shared

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import org.kouchlin.CouchDB
import org.kouchlin.test.base.mock
import org.kouchlin.util.STATUS
import org.junit.Assert.assertTrue

fun couchDBVersionTestMockedTest(couchdb: CouchDB) {

    val slot = FuelManager.instance.mock(200,
            "OK",
            "{\"couchdb\":\"Welcome\",\"version\":\"2.1.1\",\"vendor\":{\"name\":\"The Apache Software Foundation\"}}")

    assert(couchdb.version()!!.startsWith("2"))

    with(slot.captured) {
        assert(method == Method.GET)
        assert(path == "")
    }
}

fun couchDBAllDBsMockTest(couchdb: CouchDB) {

    val json = """
[
   "_users",
   "contacts",
   "docs",
   "invoices",
   "locations"
]
        """
    val slot = FuelManager.instance.mock(200,
            "OK",
            json)

    val dbs = couchdb.databases()?.size
    assert(dbs != null && dbs == 5)

    with(slot.captured) {
        assert(method == Method.GET)
        assert(path == "_all_dbs")
    }
}

fun couchDBUpdatesMockTest(couchdb: CouchDB) {
    val json = """
{
    "results":[
        {"db_name":"mailbox","type":"created","seq":"1-g1AAAAFReJzLYWBg4MhgTmHgzcvPy09JdcjLz8gvLskBCjMlMiTJ____PyuDOZExFyjAnmJhkWaeaIquGIf2JAUgmWQPMiGRAZcaB5CaePxqEkBq6vGqyWMBkgwNQAqobD4h"},
        {"db_name":"mailbox","type":"deleted","seq":"2-g1AAAAFReJzLYWBg4MhgTmHgzcvPy09JdcjLz8gvLskBCjMlMiTJ____PyuDOZEpFyjAnmJhkWaeaIquGIf2JAUgmWQPMiGRAZcaB5CaePxqEkBq6vGqyWMBkgwNQAqobD4hdQsg6vYTUncAou4-IXUPIOpA7ssCAIFHa60"}
     ],
    "last_seq": "2-g1AAAAFReJzLYWBg4MhgTmHgzcvPy09JdcjLz8gvLskBCjMlMiTJ____PyuDOZEpFyjAnmJhkWaeaIquGIf2JAUgmWQPMiGRAZcaB5CaePxqEkBq6vGqyWMBkgwNQAqobD4hdQsg6vYTUncAou4-IXUPIOpA7ssCAIFHa60"
}
        """
    val slot = FuelManager.instance.mock(200,
            "OK",
            json)
    val updates = couchdb.dbUpdates("now")

    assertTrue(updates!!.results!!.size == 2)
    assertTrue(updates.lastSeq == "2-g1AAAAFReJzLYWBg4MhgTmHgzcvPy09JdcjLz8gvLskBCjMlMiTJ____PyuDOZEpFyjAnmJhkWaeaIquGIf2JAUgmWQPMiGRAZcaB5CaePxqEkBq6vGqyWMBkgwNQAqobD4hdQsg6vYTUncAou4-IXUPIOpA7ssCAIFHa60")

    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path == "_db_updates?since=now")
    }
}

fun dbInfoMockTest(couchdb: CouchDB) {

    val json = """
{
    "cluster": {
        "n": 3,
        "q": 8,
        "r": 2,
        "w": 2
    },
    "compact_running": false,
    "data_size": 65031503,
    "db_name": "receipts",
    "disk_format_version": 6,
    "disk_size": 137433211,
    "doc_count": 6146,
    "doc_del_count": 64637,
    "instance_start_time": "0",
    "other": {
        "data_size": 66982448
    },
    "purge_seq": 0,
    "sizes": {
        "active": 65031503,
        "external": 66982448,
        "file": 137433211
    },
    "update_seq": "292786-g1AAAAF..."
}
"""
    val slot = FuelManager.instance.mock(200,
            "OK",
            json)

    val database = couchdb.database("receipts")
    assert(database.info().component1()?.dbName == "receipts")

    with(slot.captured) {
        assert(method == Method.GET)
        assert(path == "receipts")
    }
}

fun changesMockTest(couchdb: CouchDB) {

    val json = """
{
    "last_seq": "5-g1AAAAIreJyVkEsKwjAURZ-toI5cgq5A0sQ0OrI70XyppcaRY92J7kR3ojupaSPUUgotgRd4yTlwbw4A0zRUMLdnpaMkwmyF3Ily9xBwEIuiKLI05KOTW0wkV4rruP29UyGWbordzwKVxWBNOGMKZhertDlarbr5pOT3DV4gudUC9-MPJX9tpEAYx4TQASns2E24ucuJ7rXJSL1BbEgf3vTwpmedCZkYa7Pulck7Xt7x_usFU2aIHOD4eEfVTVA5KMGUkqhNZV-8_o5i",
    "pending": 0,
    "results": [
        {
            "changes": [
                {
                    "rev": "2-7051cbe5c8faecd085a3fa619e6e6337"
                }
            ],
            "id": "6478c2ae800dfc387396d14e1fc39626",
            "seq": "3-g1AAAAG3eJzLYWBg4MhgTmHgz8tPSTV0MDQy1zMAQsMcoARTIkOS_P___7MSGXAqSVIAkkn2IFUZzIkMuUAee5pRqnGiuXkKA2dpXkpqWmZeagpu_Q4g_fGEbEkAqaqH2sIItsXAyMjM2NgUUwdOU_JYgCRDA5ACGjQfn30QlQsgKvcjfGaQZmaUmmZClM8gZhyAmHGfsG0PICrBPmQC22ZqbGRqamyIqSsLAAArcXo"
        },
        {
            "changes": [
                {
                    "rev": "3-7379b9e515b161226c6559d90c4dc49f"
                }
            ],
            "deleted": true,
            "id": "5bbc9ca465f1b0fcd62362168a7c8831",
            "seq": "4-g1AAAAHXeJzLYWBg4MhgTmHgz8tPSTV0MDQy1zMAQsMcoARTIkOS_P___7MymBMZc4EC7MmJKSmJqWaYynEakaQAJJPsoaYwgE1JM0o1TjQ3T2HgLM1LSU3LzEtNwa3fAaQ_HqQ_kQG3qgSQqnoUtxoYGZkZG5uS4NY8FiDJ0ACkgAbNx2cfROUCiMr9CJ8ZpJkZpaaZEOUziBkHIGbcJ2zbA4hKsA-ZwLaZGhuZmhobYurKAgCz33kh"
        },
        {
            "changes": [
                {
                    "rev": "6-460637e73a6288cb24d532bf91f32969"
                },
                {
                    "rev": "5-eeaa298781f60b7bcae0c91bdedd1b87"
                }
            ],
            "id": "729eb57437745e506b333068fff665ae",
            "seq": "5-g1AAAAIReJyVkE0OgjAQRkcwUVceQU9g-mOpruQm2tI2SLCuXOtN9CZ6E70JFmpCCCFCmkyTdt6bfJMDwDQNFcztWWkcY8JXyB2cu49AgFwURZGloRid3MMkEUoJHbXbOxVy6arc_SxQWQzRVHCuYHaxSpuj1aqbj0t-3-AlSrZakn78oeSvjRSIkIhSNiCFHbsKN3c50b02mURvEB-yD296eNOzzoRMRLRZ98rkHS_veGcC_nR-fGe1gaCaxihhjOI2lX0BhniHaA"
        }
    ]
}
"""
    val slot = FuelManager.instance.mock(200,
            "OK",
            json)

    val database = couchdb.database("receipts")
    val (changes, etag, status2) = database.changes<Any>(includeDocs = true)
    assertTrue(status2 == STATUS.OK)
    assertTrue(changes!!.results!!.size == 3)

    assertTrue(changes.lastSeq != null)


    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path == "receipts/_changes?include_docs=true")
    }
}

