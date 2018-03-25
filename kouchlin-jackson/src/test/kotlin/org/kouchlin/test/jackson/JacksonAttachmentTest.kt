package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.util.STATUS
import java.nio.charset.Charset

class JacksonAttachmentTest : JacksonCouchDBBaseTest() {
	@Test
	fun saveAttachmentTest() {

		val database = couchdb.database("kouchlin-attachment-test-db")
		database.create()

		try {
			val document = database.document("test")
			val (response, _, status) = document.save(content = "{}")
			assert(status == STATUS.CREATED)
			assert(response!!.rev.isNotBlank())

			val attachmentRef = document.attachment("att1")

			val (result, _, status2) = attachmentRef.save(data = "This is an attachment", contentType = "text/plain", rev = response.rev)
			assert(status2 == STATUS.CREATED)
			assert(result?.rev?.isNotBlank() ?: false)

			val (attachment, etag, status3) = attachmentRef.get()
			assert(status3 == STATUS.OK)
			assert(etag?.isNotBlank() ?: false)
			assert(attachment!!.contentType == "text/plain")
			assert(attachment.data.toString(Charset.defaultCharset()) == "This is an attachment")

			val (result4, _, status4) = attachmentRef.delete(result?.rev)
			assert(status4 == STATUS.OK)
			assert(result4?.rev?.isNotBlank() ?: false)

            val (_, _, status5) = attachmentRef.get()
            assert(status5 == STATUS.NOT_FOUND)

		} finally {
			database.delete()
		}

	}

}