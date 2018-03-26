package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.ReplicationRequest

class JacksonReplicationRequest() : ReplicationRequest() {
    @JsonProperty("create_target") override var createTarget: Boolean? = null
    @JsonProperty("doc_ids") override var docIds: List<String>? = emptyList()
}
