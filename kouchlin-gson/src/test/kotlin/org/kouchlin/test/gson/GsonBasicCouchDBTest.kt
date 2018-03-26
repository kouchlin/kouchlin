package org.kouchlin.test.gson

import org.junit.Test
import org.kouchlin.test.gson.base.GsonCouchDBBaseTest

class GsonBasicCouchDBTest : GsonCouchDBBaseTest() {
    @Test
    fun couchDBUpTest() = org.kouchlin.test.shared.couchDBUpTest(couchdb)

    @Test
    fun couchDBVersionTest() = org.kouchlin.test.shared.couchDBVersionTest(couchdb)

    @Test
    fun couchDBAllDBsTest() = org.kouchlin.test.shared.couchDBAllDBsTest(couchdb)

    @Test
    fun couchDBUpdatesTest() = org.kouchlin.test.shared.couchDBUpdatesTest(couchdb)

    @Test
    fun existsDBTest() = org.kouchlin.test.shared.existsDBTest(couchdb)

    @Test
    fun notExistsDBTest() = org.kouchlin.test.shared.notExistsDBTest(couchdb)

    @Test
    fun compactTest() = org.kouchlin.test.shared.compactTest(couchdb)

    @Test
    fun ensureFullCommitTest() = org.kouchlin.test.shared.ensureFullCommitTest(couchdb)

    @Test
    fun dbInfoTest() = org.kouchlin.test.shared.dbInfoTest(couchdb)

    @Test
    fun createDBTest() = org.kouchlin.test.shared.createDBTest(couchdb)

    @Test
    fun changesTest() = org.kouchlin.test.shared.changesTest(couchdb, ::dummyJsonFactory, ::rev)
}