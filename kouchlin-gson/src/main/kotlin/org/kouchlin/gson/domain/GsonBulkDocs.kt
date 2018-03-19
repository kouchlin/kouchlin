package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.BulkDocs

class GsonBulkDocs<T> : BulkDocs<T>() {
	@SerializedName("new_edits") override var newEdits: Boolean? = null
}