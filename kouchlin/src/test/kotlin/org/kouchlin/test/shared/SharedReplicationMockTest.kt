package org.kouchlin.test.shared

import com.github.kittinunf.fuel.core.FuelManager
import org.kouchlin.CouchDB
import org.kouchlin.test.base.mock
import org.kouchlin.util.STATUS
import java.text.SimpleDateFormat
import java.util.*
import kotlin.test.assertTrue

fun replicationMockTest(couchdb: CouchDB) {
    val json = """
        {
    "history": [
        {
            "doc_write_failures": 0,
            "docs_read": 10,
            "docs_written": 10,
            "end_last_seq": "28-aaa",
            "end_time": "Sun, 11 Aug 2013 20:38:50 GMT",
            "missing_checked": 10,
            "missing_found": 10,
            "recorded_seq": "28-aaa",
            "session_id": "142a35854a08e205c47174d91b1f9628",
            "start_last_seq": "1-aaa",
            "start_time": "Sun, 11 Aug 2013 20:38:50 GMT"
        },
        {
            "doc_write_failures": 0,
            "docs_read": 1,
            "docs_written": 1,
            "end_last_seq": "28-aaa",
            "end_time": "Sat, 10 Aug 2013 15:41:54 GMT",
            "missing_checked": 1,
            "missing_found": 1,
            "recorded_seq": 1,
            "session_id": "6314f35c51de3ac408af79d6ee0c1a09",
            "start_last_seq": "0-aaa",
            "start_time": "Sat, 10 Aug 2013 15:41:54 GMT"
        }
    ],
    "ok": true,
    "replication_id_version": 3,
    "session_id": "142a35854a08e205c47174d91b1f9628",
    "source_last_seq": "28-aaa"
}
"""
    val slot = FuelManager.instance.mock(202, json = json)

    val (response, status) = couchdb.replication().source("http://127.0.0.1:5984/kouchlin-test-db")
            .target("http://127.0.0.1:5984/kouchlin-target-db")
            .trigger()

    assertTrue(status == STATUS.ACCEPTED)

    with(response!!) {
        assertTrue(ok ?: false)
        assertTrue(replicationIdVersion == 3.toLong())
        assertTrue(sessionId == "142a35854a08e205c47174d91b1f9628")
        assertTrue(sourceLastSeq == "28-aaa")
        assertTrue(history.size == 2)

        with(history.first()) {
            assertTrue(docWriteFailures == 0.toLong())
            assertTrue(docsRead == 10.toLong())
            assertTrue(docsWritten == 10.toLong())
            assertTrue(endLastSeq == "28-aaa")
            assertTrue(missingChecked == 10.toLong())
            assertTrue(missingFound == 10.toLong())
            assertTrue(recordedSeq == "28-aaa")
            assertTrue(sessionId == "142a35854a08e205c47174d91b1f9628")
            assertTrue(startLastSeq == "1-aaa")

            val date = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH).parse("Sun, 11 Aug 2013 20:38:50 GMT")

            assertTrue(endTime == date)
            assertTrue(startTime == date)

        }

    }


}