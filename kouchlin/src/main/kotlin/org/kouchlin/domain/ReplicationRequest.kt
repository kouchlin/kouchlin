package org.kouchlin.domain

abstract class ReplicationRequest() {
    var cancel: Boolean? = null
    var continuous: Boolean? = null
    abstract var createTarget: Boolean?
    abstract var docIds: List<String>?
    var filter: String? = null
    var proxy: String? = null
    var source: String? = null
    var target: String? = null
}
