package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.ViewResult

class JacksonViewResult<T> : ViewResult<T>() {
    @JsonProperty("total_rows") override var totalRows: Long? = null
    @JsonProperty("update_seq") override var updateSeq: String? = null
}