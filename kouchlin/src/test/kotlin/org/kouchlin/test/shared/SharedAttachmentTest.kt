package org.kouchlin.test.shared

import org.kouchlin.CouchDB
import org.kouchlin.util.STATUS
import java.nio.charset.Charset
import kotlin.test.assertTrue


    fun saveAttachmentTest(couchdb: CouchDB) {

        val database = couchdb.database("kouchlin-attachment-test-db")
        database.create()

        try {
            val document = database.document("test")
            val (response, _, status) = document.save(content = "{}")
            assertTrue(status == STATUS.CREATED)
            assertTrue(response!!.rev.isNotBlank())

            val attachmentRef = document.attachment("att1")

            val (result, _, status2) = attachmentRef.save(data = "This is an attachment", contentType = "text/plain", rev = response.rev)
            assertTrue(status2 == STATUS.CREATED)
            assertTrue(result?.rev?.isNotBlank() ?: false)

            val (attachment, etag, status3) = attachmentRef.get()
            assertTrue(status3 == STATUS.OK)
            assertTrue(etag?.isNotBlank() ?: false)
            assertTrue(attachment!!.contentType == "text/plain")
            assertTrue(attachment.data.toString(Charset.defaultCharset()) == "This is an attachment")

            val (result4, _, status4) = attachmentRef.delete(result?.rev)
            assertTrue(status4 == STATUS.OK)
            assertTrue(result4?.rev?.isNotBlank() ?: false)

            val (_, _, status5) = attachmentRef.get()
            assertTrue(status5 == STATUS.NOT_FOUND)

        } finally {
            database.delete()
        }

    }
