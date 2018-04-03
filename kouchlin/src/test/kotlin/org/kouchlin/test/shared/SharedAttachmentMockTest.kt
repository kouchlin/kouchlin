package org.kouchlin.test.shared

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import org.kouchlin.CouchDB
import org.kouchlin.test.base.mock
import org.kouchlin.util.ETAG_HEADER
import org.kouchlin.util.STATUS
import uy.klutter.core.uri.buildUri
import kotlin.test.assertTrue

fun existAttachmentMockTest(couchdb: CouchDB) {

    val slot = FuelManager.instance.mock(200, headers = listOf(ETAG_HEADER to "\"A-ETAG-VALUE\""))

    val database = couchdb.database("kouchlin-attachment-test-db")
    val document = database.document("test")
    val (_, etag, status) = document.attachment("attachment").exists(rev = "1-aaa")
    assertTrue(status == STATUS.OK)
    assertTrue(etag == "\"A-ETAG-VALUE\"")

   // val params = buildUri(slot.captured.url).decodedQueryDeduped
   // assertTrue(params!!["rev"] == "1-aaa")

    with(slot.captured) {
        assertTrue(method == Method.HEAD)
        assertTrue(path.startsWith("kouchlin-attachment-test-db/test/attachment"))
    }
}

fun saveAttachmentMockTest(couchdb: CouchDB) {

    val json = """
{
    "id": "test",
    "ok": true,
    "rev": "2-ce91aed0129be8f9b0f650a2edcfd0a4"
}
"""
    val slot = FuelManager.instance.mock(200, json = json)

    val database = couchdb.database("kouchlin-attachment-test-db")
    val document = database.document("test")
    val (result, _, status) = document.attachment("attachment").save("text", "plain/text", "1-aaa")
    assertTrue(status == STATUS.OK)

    with(result!!) {
        assertTrue(id == "test")
        assertTrue(ok)
        assertTrue(rev == "2-ce91aed0129be8f9b0f650a2edcfd0a4")
    }

    val params = buildUri(slot.captured.url).decodedQueryDeduped
    assertTrue(params!!["rev"] == "1-aaa")

    with(slot.captured) {
        assertTrue(method == Method.PUT)
        assertTrue(path.startsWith("kouchlin-attachment-test-db/test/attachment"))
    }
}

fun deleteAttachmentMockTest(couchdb: CouchDB) {

    val json = """
{
    "id": "test",
    "ok": true,
    "rev": "2-ce91aed0129be8f9b0f650a2edcfd0a4"
}
"""
    val slot = FuelManager.instance.mock(200, json = json)

    val database = couchdb.database("kouchlin-attachment-test-db")
    val document = database.document("test")
    val (result, _, status) = document.attachment("attachment").delete( "1-aaa")
    assertTrue(status == STATUS.OK)

    with(result!!) {
        assertTrue(id == "test")
        assertTrue(ok)
        assertTrue(rev == "2-ce91aed0129be8f9b0f650a2edcfd0a4")
    }

    val params = buildUri(slot.captured.url).decodedQueryDeduped
    assertTrue(params!!["rev"] == "1-aaa")

    with(slot.captured) {
        assertTrue(method == Method.DELETE)
        assertTrue(path.startsWith("kouchlin-attachment-test-db/test/attachment"))
    }
}