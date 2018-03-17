package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.BulkDocs

data class GsonBulkDocs<T>(override val docs: List<T>,
						   override @SerializedName("new_edits") val newEdits: Boolean? = null) : BulkDocs<T>(docs = docs, newEdits = newEdits)
