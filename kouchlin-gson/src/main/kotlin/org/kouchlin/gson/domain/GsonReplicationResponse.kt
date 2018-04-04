package org.kouchlin.gson.domain

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.HistoryEntry
import org.kouchlin.domain.ReplicationResponse
import org.kouchlin.gson.CustomDateTypeAdapter
import java.util.*


class GsonHistoryEntry:HistoryEntry() {
    @SerializedName("doc_write_failures") override var docWriteFailures: Long? = null
    @SerializedName("docs_read") override var docsRead: Long? = null
    @SerializedName("docs_written") override var docsWritten: Long? = null
    @SerializedName("end_last_seq") override var endLastSeq: String? = null

    @JsonAdapter(CustomDateTypeAdapter::class)
    @SerializedName("end_time") override var endTime: Date? = null
    @SerializedName("missing_checked") override var missingChecked: Long? = null
    @SerializedName("missing_found") override var missingFound: Long? = null
    @SerializedName("recorded_seq") override var recordedSeq: String? = null
    @SerializedName("session_id") override var sessionId: String? = null
    @SerializedName("start_last_seq") override var startLastSeq: String? = null

    @JsonAdapter(CustomDateTypeAdapter::class)
    @SerializedName("start_time")
    override var startTime: Date? = null
}

class GsonReplicationResponse : ReplicationResponse<GsonHistoryEntry>()  {
    @SerializedName("replication_id_version") override var replicationIdVersion: Long? = null
    @SerializedName("session_id") override var sessionId: String? = null
    @SerializedName("source_last_seq") override var sourceLastSeq: String? = null
}
