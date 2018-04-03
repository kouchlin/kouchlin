package org.kouchlin.test.jackson

import org.junit.Test
import org.kouchlin.test.jackson.base.JacksonCouchDBBaseMockTest

class JacksonAttachmentMockTest : JacksonCouchDBBaseMockTest() {

    @Test
    fun existAttachmentMockTest() = org.kouchlin.test.shared.existAttachmentMockTest(couchdb)

    @Test
    fun saveAttachmentMockTest() = org.kouchlin.test.shared.saveAttachmentMockTest(couchdb)

    @Test
    fun deleteAttachmentMockTest() = org.kouchlin.test.shared.deleteAttachmentMockTest(couchdb)

}