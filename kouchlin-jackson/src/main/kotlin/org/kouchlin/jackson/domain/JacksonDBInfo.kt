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
import org.kouchlin.domain.Cluster
import org.kouchlin.domain.DBInfo

class JacksonDBInfo : DBInfo() {
	@JsonProperty("compact_running") override var compactRunning: Boolean? = null
	@JsonProperty("data_size") override var dataSize: Long? = null
	@JsonProperty("db_name") override var dbName: String? = null
	@JsonProperty("disk_format_version") override var diskFormatVersion: Int? = null
	@JsonProperty("disk_size") override var diskSize: Long? = null
	@JsonProperty("doc_count") override var docCount: Long? = null
	@JsonProperty("doc_del_count") override var docDelCount: Long? = null
	@JsonProperty("instance_start_time") override var instanceStartTime: Long? = null
	@JsonProperty("purge_seq") override var purgeSeq: Long? = null
	@JsonProperty("update_seq") override var updateSeq: String? = null
	@JsonProperty("other.data_size") override var otherDataSize: Long? = null
	@JsonProperty("sizes.active") override var activeSize: Long? = null
	@JsonProperty("sizes.external") override var externalSize: Long? = null
	@JsonProperty("sizes.file") override var fileSize: Long? = null
}