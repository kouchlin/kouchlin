package org.kouchlin.domain

data class ViewRevRow (val rev:String)
data class ViewResultRow<T,D> (val id:String, val key:String, var value:T?, var doc:D?)

abstract class ViewResult<T> {
    var offset:Long?=null
    var rows:List<T> = emptyList()
    abstract var totalRows:Long?
    abstract var updateSeq:String?
}