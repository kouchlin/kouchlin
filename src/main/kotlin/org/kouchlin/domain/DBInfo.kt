package org.kouchlin.domain

import com.google.gson.annotations.SerializedName

data class Cluster(val n: Int, val q: Int, val r: Int, val w: Int)
data class DBInfo(val cluster: Cluster?,
				  @SerializedName("compact_running") val compactRunning: Boolean,
				  @SerializedName("data_size") val dataSize: Long,
				  @SerializedName("db_name") val dbName: String,
				  @SerializedName("disk_format_version") val diskFormatVersion: Int,
				  @SerializedName("disk_size") val diskSize: Long,
				  @SerializedName("doc_count") val docCount: Long,
				  @SerializedName("doc_del_count") val docDelCount: Long,
				  @SerializedName("instance_start_time") val instanceStartTime: Int,
				  @SerializedName("purge_seq") val purgeSeq: Long,
				  @SerializedName("update_seq") val updateSeq: String,
				  @SerializedName("other.data_size") val otherDataSize: Long,
				  @SerializedName("sizes.active") val activeSize: Long,
				  @SerializedName("sizes.external") val externalSize: Long,
				  @SerializedName("sizes.file") val fileSize: Long
)
