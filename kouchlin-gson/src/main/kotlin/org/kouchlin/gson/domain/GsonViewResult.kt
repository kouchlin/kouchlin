package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.ViewResult

class GsonViewResult<T> : ViewResult<T>() {
    @SerializedName("total_rows") override var totalRows: Long? = null
    @SerializedName("update_seq") override var updateSeq: String? = null
}