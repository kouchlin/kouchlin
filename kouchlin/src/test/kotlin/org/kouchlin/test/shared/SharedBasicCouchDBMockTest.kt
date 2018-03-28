package org.kouchlin.test.shared

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import org.kouchlin.CouchDB
import org.kouchlin.test.base.mock

fun couchDBVersionTestMockedTest(couchdb: CouchDB) {

    val slot = FuelManager.instance.mock(200,
            "OK",
            "{\"couchdb\":\"Welcome\",\"version\":\"2.1.1\",\"vendor\":{\"name\":\"The Apache Software Foundation\"}}")

    assert(!couchdb.version()!!.startsWith("2"))

    with(slot.captured) {
        assert(method == Method.GET)
        assert(path == "")
    }
}

