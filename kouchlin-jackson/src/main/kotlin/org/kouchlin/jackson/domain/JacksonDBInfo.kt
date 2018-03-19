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