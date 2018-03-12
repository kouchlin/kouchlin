package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.Cluster
import org.kouchlin.domain.DBInfo

data class JacksonDBInfo(@JsonProperty("cluster") override val cluster: Cluster?,
					  @JsonProperty("compact_running") override val compactRunning: Boolean,
					  @JsonProperty("data_size") override val dataSize: Long,
					  @JsonProperty("db_name") override val dbName: String,
					  @JsonProperty("disk_format_version") override val diskFormatVersion: Int,
					  @JsonProperty("disk_size") override val diskSize: Long,
					  @JsonProperty("doc_count") override val docCount: Long,
					  @JsonProperty("doc_del_count") override val docDelCount: Long,
					  @JsonProperty("instance_start_time") override val instanceStartTime: Int,
					  @JsonProperty("purge_seq") override val purgeSeq: Long,
					  @JsonProperty("update_seq") override val updateSeq: String,
					  @JsonProperty("other.data_size") override val otherDataSize: Long,
					  @JsonProperty("sizes.active") override val activeSize: Long,
					  @JsonProperty("sizes.external") override val externalSize: Long,
					  @JsonProperty("sizes.file") override val fileSize: Long
) : DBInfo(cluster, compactRunning, dataSize, dbName, diskFormatVersion, diskSize,
		docCount, docDelCount, instanceStartTime, purgeSeq, updateSeq, otherDataSize, activeSize, externalSize, fileSize)