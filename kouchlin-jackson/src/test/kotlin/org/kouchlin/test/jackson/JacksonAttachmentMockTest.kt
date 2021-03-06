/*
 * Copyright 2018 Kouchlin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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