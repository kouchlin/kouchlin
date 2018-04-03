package org.kouchlin.test.shared

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import org.kouchlin.CouchDB
import org.kouchlin.test.base.mock
import org.kouchlin.util.CONTENT_LENGHT_HEADER
import org.kouchlin.util.ETAG_HEADER
import org.kouchlin.util.IF_NONE_MATCH_HEADER
import org.kouchlin.util.STATUS
import uy.klutter.core.uri.UriBuilder
import uy.klutter.core.uri.buildUri
import java.net.URI
import java.net.URL
import kotlin.test.assertTrue

fun notExistsDocMockTest(couchdb: CouchDB) {

    val slot = FuelManager.instance.mock(404)

    val database = couchdb.database("kouchlin-test-db")
    val (_, _, status) = database.document("test1").exists()
    assertTrue(status == STATUS.NOT_FOUND)

    with(slot.captured) {
        assertTrue(method == Method.HEAD)
        assertTrue(path == "kouchlin-test-db/test1")
    }
}

fun existsDocMockTest(couchdb: CouchDB) {

    val slot = FuelManager.instance.mock(statusCode = 200,
            headers = listOf(ETAG_HEADER to "\"A-ETAG-VAL\"",
                    CONTENT_LENGHT_HEADER to "1"))

    val database = couchdb.database("kouchlin-test-db")
    val (contentLenght, etag, status) = database.document("test1").exists()
    assertTrue(status == STATUS.OK)
    assertTrue(contentLenght == 1)
    assertTrue(etag == "\"A-ETAG-VAL\"")

    with(slot.captured) {
        assertTrue(method == Method.HEAD)
        assertTrue(path == "kouchlin-test-db/test1")
    }
}

fun getNotFoundDocStringMockTest(couchdb: CouchDB) {

    val slot = FuelManager.instance.mock(statusCode = 404)

    val database = couchdb.database("kouchlin-test-db")
    val (_, _, status) = database.document("test2")
            .get<String>()
    assertTrue(status == STATUS.NOT_FOUND)

    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path == "kouchlin-test-db/test2")
    }
}

fun getDocStringMockTest(couchdb: CouchDB) {

    val json = """
        {"_id":"test2"}
        """
    val slot = FuelManager.instance.mock(statusCode = 200,
            json = json,
            headers = listOf(ETAG_HEADER to "\"A-ETAG-VAL\""))

    val database = couchdb.database("kouchlin-test-db")
    val (doc, etag, status) = database.document("test2").get<String>()

    assertTrue(status == STATUS.OK)
    assertTrue(doc == json)
    assertTrue(etag == "\"A-ETAG-VAL\"")

    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path == "kouchlin-test-db/test2")
    }
}

fun getDocStringParamsMockTest(couchdb: CouchDB) {
    val slot = FuelManager.instance.mock(statusCode = 404)

    val database = couchdb.database("kouchlin-test-db")
    val (_, _, _) = database.document("test2").get<String>(
            attachment = true,
            attEncodingInfo = true,
            attsSince = listOf("1-aaa", "2-bbb"),
            conflicts = true,
            deletedConflicts = true,
            etag = "\"A-ETAG-VALUE\"",
            latest = true,
            localSeq = true,
            meta = true,
            openRevs = listOf("1-aaa", "2-bbb"),
            rev = "1-aaa",
            revs = true,
            revsInfo = true
    )
    val params = buildUri(slot.captured.url).decodedQueryDeduped

    assertTrue(params!!["attachment"]?.toBoolean() ?: false)
    assertTrue(params["att_encoding_info"]?.toBoolean() ?: false)
    assertTrue(params["conflicts"]?.toBoolean() ?: false)
    assertTrue(params["deleted_conflicts"]?.toBoolean() ?: false)
    assertTrue(params["latest"]?.toBoolean() ?: false)
    assertTrue(params["local_seq"]?.toBoolean() ?: false)
    assertTrue(params["meta"]?.toBoolean() ?: false)
    assertTrue(params["rev"] == "1-aaa")
    assertTrue(params["revs"]?.toBoolean() ?: false)
    assertTrue(params["revs_info"]?.toBoolean() ?: false)
    assertTrue(params["atts_since"] == "[\"1-aaa\",\"2-bbb\"]")
    assertTrue(params["open_revs"] == "[\"1-aaa\",\"2-bbb\"]")

    assertTrue(slot.captured.headers[IF_NONE_MATCH_HEADER] == "\"A-ETAG-VALUE\"")
}

fun putDocStringMockTest(couchdb: CouchDB) {

    val json = """
        {"id":"test","ok":true,"rev":"1-aaa"}
        """
    val slot = FuelManager.instance.mock(statusCode = 201,
            json = json,
            headers = listOf(ETAG_HEADER to "\"A-ETAG-VALUE\""))

    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (response, etag, status) = database.document("test").save(content = doc,
            rev = "1-aaa",
            batch = true,
            newEdits = true)

    assertTrue(status == STATUS.CREATED)
    assertTrue(etag == "\"A-ETAG-VALUE\"")

    with(response!!) {
        assertTrue(id == "test")
        assertTrue(rev == "1-aaa")
        assertTrue(ok)
    }

    val params = buildUri(slot.captured.url).decodedQueryDeduped

    assertTrue(params!!["batch"] == "ok")
    assertTrue(params["new_edits"]?.toBoolean() ?: false)
    assertTrue(params["rev"] == "1-aaa")

    with(slot.captured) {
        assertTrue(method == Method.PUT)
        assertTrue(path.startsWith("kouchlin-test-db/test"))
    }
}

fun postDocStringMockTest(couchdb: CouchDB) {

    val json = """
        {"id":"test","ok":true,"rev":"1-aaa"}
        """
    val slot = FuelManager.instance.mock(statusCode = 201,
            json = json,
            headers = listOf(ETAG_HEADER to "\"A-ETAG-VALUE\""))

    val database = couchdb.database("kouchlin-test-db")
    val doc = """{
					"a":1
					}"""
    val (response, etag, status) = database.document().save(content = doc,
            rev = "1-aaa",
            batch = true,
            newEdits = true)

    assertTrue(status == STATUS.CREATED)
    assertTrue(etag == "\"A-ETAG-VALUE\"")

    with(response!!) {
        assertTrue(id == "test")
        assertTrue(rev == "1-aaa")
        assertTrue(ok)
    }

    val params = buildUri(slot.captured.url).decodedQueryDeduped

    assertTrue(params!!["batch"] == "ok")
    assertTrue(params["new_edits"]?.isEmpty() ?: true)
    assertTrue(params["rev"]?.isEmpty() ?: true)

    with(slot.captured) {
        assertTrue(method == Method.POST)
        assertTrue(path.startsWith("kouchlin-test-db"))
    }
}

fun <T : Any> putDocTestFromObjectMockTest(couchdb: CouchDB, factory: (String?, String?, String) -> T) {

    val json = """
        {"id":"test_with_id","ok":true,"rev":"1-aaa"}
        """
    val slot = FuelManager.instance.mock(statusCode = 201,
            json = json,
            headers = listOf(ETAG_HEADER to "\"A-ETAG-VALUE\""))

    val database = couchdb.database("kouchlin-test-db")
    val doc = factory("test_with_id", null, "value")
    val (_, _, status) = database.document().save(content = doc)
    assertTrue(status == STATUS.CREATED)
    with(slot.captured) {
        assertTrue(method == Method.PUT)
        assertTrue(path.startsWith("kouchlin-test-db/test_with_id"))
    }
}

fun <T : Any> postDocTestFromObjectMockTest(couchdb: CouchDB, factory: (String?, String?, String) -> T) {

    val json = """
        {"id":"test_with_id","ok":true,"rev":"1-aaa"}
        """
    val slot = FuelManager.instance.mock(statusCode = 201,
            json = json,
            headers = listOf(ETAG_HEADER to "\"A-ETAG-VALUE\""))

    val database = couchdb.database("kouchlin-test-db")
    val doc = factory(null, null, "value")
    val (_, _, status) = database.document().save(content = doc)
    assertTrue(status == STATUS.CREATED)
    with(slot.captured) {
        assertTrue(method == Method.POST)
        assertTrue(path == "kouchlin-test-db")
    }

}

fun allDocsParamsMockTest(couchdb: CouchDB) {
    val json = """
        {
    "offset": 0,
    "rows": [
        {
            "id": "16e458537602f5ef2a710089dffd9453",
            "key": "16e458537602f5ef2a710089dffd9453",
            "value": {
                "rev": "1-967a00dff5e02add41819138abb3284d"
            }
        },
        {
            "id": "a4c51cdfa2069f3e905c431114001aff",
            "key": "a4c51cdfa2069f3e905c431114001aff",
            "value": {
                "rev": "1-967a00dff5e02add41819138abb3284d"
            }
        },
        {
            "id": "a4c51cdfa2069f3e905c4311140034aa",
            "key": "a4c51cdfa2069f3e905c4311140034aa",
            "value": {
                "rev": "5-6182c9c954200ab5e3c6bd5e76a1549f"
            }
        },
        {
            "id": "a4c51cdfa2069f3e905c431114003597",
            "key": "a4c51cdfa2069f3e905c431114003597",
            "value": {
                "rev": "2-7051cbe5c8faecd085a3fa619e6e6337"
            }
        },
        {
            "id": "f4ca7773ddea715afebc4b4b15d4f0b3",
            "key": "f4ca7773ddea715afebc4b4b15d4f0b3",
            "value": {
                "rev": "2-7051cbe5c8faecd085a3fa619e6e6337"
            }
        }
    ],
    "total_rows": 5
}
 """

    val slot = FuelManager.instance.mock(statusCode = 200, json = json)

    val database = couchdb.database("kouchlin-test-db")
    val (result, status) = database.allDocs<Any>(
            conflicts = true,
            descending = true,
            endKey = "Z-key",
            endKeyDocId = "Z-key-docId",
            group = true,
            groupLevel = 1,
            includeDocs = true,
            attachments = true,
            attEncodingInfo = true,
            inclusiveEnd = true,
            key = "B-key",
            keys = listOf("A-key", "B-key"),
            limit = 1,
            reduce = true,
            skip = 1,
            sorted = true,
            stable = true,
            stale = "ok",
            startKey = "A-key",
            startKeyDocId = "A-key-docId",
            update = "true",
            updateSeq = true
    )

    assertTrue(status == STATUS.OK)

    val params = buildUri(slot.captured.url).decodedQueryDeduped

    assertTrue(params!!["conflicts"]?.toBoolean() ?: false)
    assertTrue(params["descending"]?.toBoolean() ?: false)
    assertTrue(params["endkey"] == "Z-key")
    assertTrue(params["endkey_docid"] == "Z-key-docId")
    assertTrue(params["group"]?.toBoolean() ?: false)
    assertTrue(params["group_level"] == "1")
    assertTrue(params["include_docs"]?.toBoolean() ?: false)
    assertTrue(params["attachments"]?.toBoolean() ?: false)
    assertTrue(params["att_encoding_info"]?.toBoolean() ?: false)
    assertTrue(params["inclusive_end"]?.toBoolean() ?: false)
    assertTrue(params["key"] == "B-key")
    assertTrue(params["keys"] == "[\"A-key\",\"B-key\"]")
    assertTrue(params["limit"] == "1")
    assertTrue(params["reduce"]?.toBoolean() ?: false)
    assertTrue(params["skip"] == "1")
    assertTrue(params["sorted"]?.toBoolean() ?: false)
    assertTrue(params["stable"]?.toBoolean() ?: false)
    assertTrue(params["stale"] == "ok")
    assertTrue(params["startkey"] == "A-key")
    assertTrue(params["startkey_docid"] == "A-key-docId")
    assertTrue(params["update"] == "true")
    assertTrue(params["update_seq"]?.toBoolean() ?: false)

    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path.startsWith("kouchlin-test-db/_all_docs"))
    }
}


fun allDocsMockTest(couchdb: CouchDB) {
    val json = """
        {
    "offset": 0,
    "rows": [
        {
            "id": "16e458537602f5ef2a710089dffd9453",
            "key": "16e458537602f5ef2a710089dffd9453",
            "value": {
                "rev": "1-967a00dff5e02add41819138abb3284d"
            }
        },
        {
            "id": "a4c51cdfa2069f3e905c431114001aff",
            "key": "a4c51cdfa2069f3e905c431114001aff",
            "value": {
                "rev": "1-967a00dff5e02add41819138abb3284d"
            }
        },
        {
            "id": "a4c51cdfa2069f3e905c4311140034aa",
            "key": "a4c51cdfa2069f3e905c4311140034aa",
            "value": {
                "rev": "5-6182c9c954200ab5e3c6bd5e76a1549f"
            }
        },
        {
            "id": "a4c51cdfa2069f3e905c431114003597",
            "key": "a4c51cdfa2069f3e905c431114003597",
            "value": {
                "rev": "2-7051cbe5c8faecd085a3fa619e6e6337"
            }
        },
        {
            "id": "f4ca7773ddea715afebc4b4b15d4f0b3",
            "key": "f4ca7773ddea715afebc4b4b15d4f0b3",
            "value": {
                "rev": "2-7051cbe5c8faecd085a3fa619e6e6337"
            }
        }
    ],
    "total_rows": 5,
    "update_seq":"444-g1AA"
}
 """

    val slot = FuelManager.instance.mock(statusCode = 200, json = json)

    val database = couchdb.database("kouchlin-test-db")
    val (result, status) = database.allDocs<Any>()

    assertTrue(status == STATUS.OK)
    with(result!!) {
        assertTrue(totalRows == 5.toLong())
        assertTrue(offset == 0.toLong())
        assertTrue(rows.size == 5)
        assertTrue(updateSeq == "444-g1AA")

        with(rows.first()) {
            assertTrue(doc == null)
            assertTrue(id == "16e458537602f5ef2a710089dffd9453")
            assertTrue(key == "16e458537602f5ef2a710089dffd9453")
            assertTrue(value!!.rev == "1-967a00dff5e02add41819138abb3284d")
        }

    }

    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path.startsWith("kouchlin-test-db/_all_docs"))
    }
}

inline fun <reified T : Any> allDocsWithDocMockTest(couchdb: CouchDB) {
    val json = """
        {
    "offset": 0,
    "rows": [
        {
            "id": "16e458537602f5ef2a710089dffd9453",
            "key": "16e458537602f5ef2a710089dffd9453",
            "value": {
                "rev": "1-967a00dff5e02add41819138abb3284d"
            },
            "doc": { "_id":"test_with_id1","_rev":"1-967a00dff5e02add41819138abb3284d","foo":"value"}
        },
        {
            "id": "a4c51cdfa2069f3e905c431114001aff",
            "key": "a4c51cdfa2069f3e905c431114001aff",
            "value": {
                "rev": "1-967a00dff5e02add41819138abb3284d"
            },
            "doc": { "_id":"test_with_id2","_rev":"1-967a00dff5e02add41819138abb3284d","foo":"value"}
        },
        {
            "id": "a4c51cdfa2069f3e905c4311140034aa",
            "key": "a4c51cdfa2069f3e905c4311140034aa",
            "value": {
                "rev": "5-6182c9c954200ab5e3c6bd5e76a1549f"
            },
            "doc": { "_id":"test_with_id3","_rev":"5-6182c9c954200ab5e3c6bd5e76a1549f","foo":"value"}
        }
    ],
    "total_rows": 3,
    "update_seq":"444-g1AA"
}
 """

    val slot = FuelManager.instance.mock(statusCode = 200, json = json)

    val database = couchdb.database("kouchlin-test-db")
    val (result, status) = database.allDocs<T>(includeDocs = true)

    assertTrue(status == STATUS.OK)
    with(result!!) {
        assertTrue(totalRows == 3.toLong())
        assertTrue(offset == 0.toLong())
        assertTrue(rows.size == 3)
        assertTrue(updateSeq == "444-g1AA")

        with(rows.first()) {
            assertTrue(doc is T)
            assertTrue(id == "16e458537602f5ef2a710089dffd9453")
            assertTrue(key == "16e458537602f5ef2a710089dffd9453")
            assertTrue(value!!.rev == "1-967a00dff5e02add41819138abb3284d")
        }
    }

    with(slot.captured) {
        assertTrue(method == Method.GET)
        assertTrue(path.startsWith("kouchlin-test-db/_all_docs"))
    }
}

fun bulkDocsMockTest(couchdb: CouchDB) {
    val json = """
        [
    {
        "ok": true,
        "id": "FishStew",
        "rev":"1-967a00dff5e02add41819138abb3284d"
    },
    {
        "ok": true,
        "id": "LambStew",
        "rev": "3-f9c62b2169d0999103e9f41949090807"
    }
]
"""
    val slot = FuelManager.instance.mock(statusCode = 201, json = json)

    val database = couchdb.database("kouchlin-test-db")
    val (result, status) = database.bulkDocs(emptyList(), true)
    assertTrue(status == STATUS.CREATED)
    assertTrue(result?.size == 2)

    with(result!!.first()) {
        assertTrue(ok)
        assertTrue(id=="FishStew")
        assertTrue(rev=="1-967a00dff5e02add41819138abb3284d")
    }

    with(slot.captured) {
        assertTrue(method == Method.POST)
        assertTrue(path.startsWith("kouchlin-test-db/_bulk_docs"))
    }
}

