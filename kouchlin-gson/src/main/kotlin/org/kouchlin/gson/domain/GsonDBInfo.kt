package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.Cluster
import org.kouchlin.domain.DBInfo

data class GsonDBInfo(@SerializedName("cluster") override val cluster: Cluster?,
					  @SerializedName("compact_running") override val compactRunning: Boolean,
					  @SerializedName("data_size") override val dataSize: Long,
					  @SerializedName("db_name") override val dbName: String,
					  @SerializedName("disk_format_version") override val diskFormatVersion: Int,
					  @SerializedName("disk_size") override val diskSize: Long,
					  @SerializedName("doc_count") override val docCount: Long,
					  @SerializedName("doc_del_count") override val docDelCount: Long,
					  @SerializedName("instance_start_time") override val instanceStartTime: Int,
					  @SerializedName("purge_seq") override val purgeSeq: Long,
					  @SerializedName("update_seq") override val updateSeq: String,
					  @SerializedName("other.data_size") override val otherDataSize: Long,
					  @SerializedName("sizes.active") override val activeSize: Long,
					  @SerializedName("sizes.external") override val externalSize: Long,
					  @SerializedName("sizes.file") override val fileSize: Long
) : DBInfo(cluster, compactRunning, dataSize, dbName, diskFormatVersion, diskSize,
		docCount, docDelCount, instanceStartTime, purgeSeq, updateSeq, otherDataSize, activeSize, externalSize, fileSize)