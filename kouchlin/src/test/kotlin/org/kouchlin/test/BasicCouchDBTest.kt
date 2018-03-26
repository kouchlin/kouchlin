package org.kouchlin.test

import org.junit.Test
import org.kouchlin.test.base.CouchDBBaseTest

class BasicCouchDBTest : CouchDBBaseTest() {


    @Test
    fun couchDBUpTest() = org.kouchlin.test.shared.couchDBUpTest(couchdb)

    @Test
    fun existsDBTest() = org.kouchlin.test.shared.existsDBTest(couchdb)

    @Test
    fun notExistsDBTest() = org.kouchlin.test.shared.notExistsDBTest(couchdb)

    @Test
    fun compactTest() = org.kouchlin.test.shared.compactTest(couchdb)

    @Test
    fun ensureFullCommitTest() = org.kouchlin.test.shared.ensureFullCommitTest(couchdb)

    @Test
    fun createDBTest() = org.kouchlin.test.shared.createDBTest(couchdb)
}