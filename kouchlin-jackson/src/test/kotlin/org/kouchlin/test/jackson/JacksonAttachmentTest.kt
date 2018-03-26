package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.test.jackson.base.JacksonCouchDBBaseTest

class JacksonAttachmentTest : JacksonCouchDBBaseTest() {
    @Test
    fun saveAttachmentTest() = org.kouchlin.test.shared.saveAttachmentTest(couchdb)
}