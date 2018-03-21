package org.kouchlin.domain

data class ChangesResultRev(val rev: String)

data class ChangesResult<T>(var seq: String,
							var id: String,
							var deleted: Boolean?,
							var changes: List<ChangesResultRev>,
							var doc: T?
)

abstract class Changes<T> {
	abstract var lastSeq: String?
	var pending: Long? = null
	var results: List<ChangesResult<T>>?=null
}
