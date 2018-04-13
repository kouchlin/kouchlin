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

package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.HistoryEntry
import org.kouchlin.domain.ReplicationResponse
import java.util.*

class JacksonHistoryEntry : HistoryEntry() {
    @JsonProperty("doc_write_failures") override var docWriteFailures: Long? = null
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
