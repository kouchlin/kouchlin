package org.kouchlin.domain

open class BulkDocs<T>(@Transient open val docs: List<T>,
					   @Transient open val newEdits: Boolean?=null)

data class BulkDocsResult(val ok: Boolean, val id: String?, val rev: String?)  