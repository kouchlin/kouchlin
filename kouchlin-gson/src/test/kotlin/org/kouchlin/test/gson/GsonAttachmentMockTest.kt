package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseMockTest

class GsonAttachmentMockTest : GsonCouchDBBaseMockTest() {

    @Test
    fun existAttachmentMockTest() = org.kouchlin.test.shared.existAttachmentMockTest(couchdb)

    @Test
    fun saveAttachmentMockTest() = org.kouchlin.test.shared.saveAttachmentMockTest(couchdb)

    @Test
    fun deleteAttachmentMockTest() = org.kouchlin.test.shared.deleteAttachmentMockTest(couchdb)

}