package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.ReplicationRequest

class GsonReplicationRequest() : ReplicationRequest() {
    @SerializedName("create_target") override var createTarget: Boolean? = null
    @SerializedName("doc_ids") override var docIds: List<String>? = emptyList()
}
