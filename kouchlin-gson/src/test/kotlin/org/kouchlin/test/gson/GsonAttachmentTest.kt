package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseTest

class GsonAttachmentTest : GsonCouchDBBaseTest() {
    @Test
    fun saveAttachmentTest() = org.kouchlin.test.shared.saveAttachmentTest(couchdb)
}