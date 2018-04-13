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

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.Cluster
import org.kouchlin.domain.DBInfo

class GsonDBInfo : DBInfo() {
	@SerializedName("compact_running") override var compactRunning: Boolean? = null
	@SerializedName("data_size") override var dataSize: Long? = null
	@SerializedName("db_name") override var dbName: String? = null
	@SerializedName("disk_format_version") override var diskFormatVersion: Int? = null
	@SerializedName("disk_size") override var diskSize: Long? = null
	@SerializedName("doc_count") override var docCount: Long? = null
	@SerializedName("doc_del_count") override var docDelCount: Long? = null
	@SerializedName("instance_start_time") override var instanceStartTime: Long? = null
	@SerializedName("purge_seq") override var purgeSeq: Long? = null
	@SerializedName("update_seq") override var updateSeq: String? = null
	@SerializedName("other.data_size") override var otherDataSize: Long? = null
	@SerializedName("sizes.active") override var activeSize: Long? = null
	@SerializedName("sizes.external") override var externalSize: Long? = null
	@SerializedName("sizes.file") override var fileSize: Long? = null
}