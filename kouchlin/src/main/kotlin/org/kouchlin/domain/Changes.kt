package org.kouchlin.domain

data class ChangesResultRev(val rev: String)

data class ChangesResult<T>(val seq: String,
							val id: String,
							val deleted: Boolean?,
							val changes: List<ChangesResultRev>,
							val doc: T?
)

abstract class Changes<T> {
	abstract var lastSeq: String?
	var pending: Long? = null
	var results: List<ChangesResult<T>>?=null
}
