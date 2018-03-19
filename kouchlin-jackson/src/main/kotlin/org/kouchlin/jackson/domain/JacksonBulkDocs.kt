package org.kouchlin.jackson.domain

import org.kouchlin.domain.BulkDocs
import com.fasterxml.jackson.annotation.JsonProperty

class JacksonBulkDocs<T> : BulkDocs<T>() {
	override @JsonProperty("new_edits") var newEdits: Boolean? = null
}

