package org.kouchlin.domain

data class ViewRevRow (val rev:String)
data class ViewResultRow<T,D> (val id:String, val key:String, var value:T?, var doc:D?)
data class ViewResult<T> (val offset:Long, var rows:List<T>, val total_rows:Long, val updateSeq:String?=null ) {
}