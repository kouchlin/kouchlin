package org.kouchlin.gson.domain

import org.kouchlin.domain.Changes
import org.kouchlin.domain.ChangesResult
import com.fasterxml.jackson.annotation.JsonProperty

class JacksonChanges<T> : Changes<T>() {
	@JsonProperty("last_seq") override var lastSeq: String? = null
}
