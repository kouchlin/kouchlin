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

package org.kouchlin.domain

import java.util.*

/*
doc_write_failures (number) – Number of document write failures
docs_read (number) – Number of documents read
docs_written (number) – Number of documents written to target
end_last_seq (number) – Last sequence number in changes stream
end_time (string) – Date/Time replication operation completed in RFC 2822 format
missing_checked (number) – Number of missing documents checked
missing_found (number) – Number of missing documents found
recorded_seq (number) – Last recorded sequence number
session_id (string) – Session ID for this replication operation
start_last_seq (number) – First sequence number in changes stream
start_time (string) – Date/Time replication operation started in RFC 2822 format
 */

abstract class HistoryEntry {
    abstract var docWriteFailures: Long?
    abstract var docsRead: Long?
    abstract var docsWritten: Long?
    abstract var endLastSeq: String?
    abstract var endTime: Date?
    abstract var missingChecked: Long?
    abstract var missingFound: Long?
    abstract var recordedSeq: String?
    abstract var sessionId: String?
    abstract var startLastSeq: String?
    abstract var startTime: Date?

}

abstract class ReplicationResponse<T : HistoryEntry> {
    var history: List<T> = emptyList()
    var ok: Boolean? = null
    abstract var replicationIdVersion: Long?
    abstract var sessionId: String?
    abstract var sourceLastSeq: String?
}
