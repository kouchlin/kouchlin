package org.kouchlin.domain

abstract class BulkDocs<T> {
	var docs: List<T>? = null
	abstract var newEdits: Boolean?
}

data class BulkDocsResult(val ok: Boolean, val id: String?, val rev: String?)  