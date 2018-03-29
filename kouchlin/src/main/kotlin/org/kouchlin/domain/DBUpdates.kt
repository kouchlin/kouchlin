package org.kouchlin.domain

abstract class DBUpdatesResult {
    abstract var dbName: String?
    var type: String? = null
    var seq: String? = null
}

abstract class DBUpdates<T> {
    var results: List<T>? = null
    abstract var lastSeq: String?
}