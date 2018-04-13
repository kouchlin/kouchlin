/*
 * Copyright 2018 Kouchlin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
