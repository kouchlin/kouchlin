package org.kouchlin.gson.domain

import org.kouchlin.domain.Changes
import org.kouchlin.domain.ChangesResult
import com.google.gson.annotations.SerializedName

class GsonChanges<T> : Changes<T>() {
	@SerializedName("last_seq") override var lastSeq: String? = null
}