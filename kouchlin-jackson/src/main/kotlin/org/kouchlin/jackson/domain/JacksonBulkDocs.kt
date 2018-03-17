package org.kouchlin.jackson.domain

import org.kouchlin.domain.BulkDocs
import com.fasterxml.jackson.annotation.JsonProperty

data class JacksonBulkDocs<T>(override val docs: List<T>,
						   override @JsonProperty("new_edits") val newEdits: Boolean? = null) : BulkDocs<T>(docs = docs, newEdits = newEdits)
