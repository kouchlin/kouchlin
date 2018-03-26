package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.HistoryEntry
import org.kouchlin.domain.ReplicationResponse
import java.util.*

class JacksonHistoryEntry : HistoryEntry() {
    @JsonProperty("docs_write_failures") override var docWriteFailures: Long? = null
    @JsonProperty("docs_read") override var docsRead: Long? = null
    @JsonProperty("docs_written") override var docsWritten: Long? = null
    @JsonProperty("end_last_seq") override var endLastSeq: String? = null
    @JsonProperty("end_time") override var endTime: Date? = null
    @JsonProperty("missing_checked") override var missingChecked: Long? = null
    @JsonProperty("missing_found") override var missingFound: Long? = null
    @JsonProperty("recorded_seq") override var recordedSeq: String? = null
    @JsonProperty("session_id") override var sessionId: String? = null
    @JsonProperty("start_last_seq") override var startLastSeq: String? = null
    @JsonProperty("start_time") override var startTime: Date? = null
}

class JacksonReplicationResponse : ReplicationResponse<JacksonHistoryEntry>()  {
    @JsonProperty("replication_id_version") override var replicationIdVersion: Long? = null
    @JsonProperty("session_id") override var sessionId: String? = null
    @JsonProperty("source_last_seq") override var sourceLastSeq: String? = null
}
