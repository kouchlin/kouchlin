package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.util.STATUS

class GsonAttachmentTest : GsonCouchDBBaseTest() {
//	@Test
	fun saveAttachmentTest() {
		var database = couchdb.database("kouchlin-attachment-test-db")
		database.create()
		var document = database.document("test")
		val (response, _, status) = document.save(content = "{}")
		assert(status == STATUS.CREATED)
		assert(response!!.rev.isNotBlank())
		
		val (_, status2) = document.attachment("att1").save(data = "This is an attachment", contentType = "text/plain", rev=response.rev)
		assert(status2 == STATUS.CREATED)

		val (attachment, contentType, status3) = document.attachment("att1").get()
		assert(status3 == STATUS.OK)
		assert(contentType == "text/plain")
		assert(attachment!!.data.contentToString() == "This is an attachment")

	}

}