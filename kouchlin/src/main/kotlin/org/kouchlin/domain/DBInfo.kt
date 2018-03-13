package org.kouchlin.domain

data class Cluster(val n: Int, val q: Int, val r: Int, val w: Int)

open class DBInfo(@Transient open val cluster: Cluster?,
				  @Transient open val compactRunning: Boolean,
				  @Transient open val dataSize: Long,
				  @Transient open val dbName: String,
				  @Transient open val diskFormatVersion: Int,
				  @Transient open val diskSize: Long,
				  @Transient open val docCount: Long,
				  @Transient open val docDelCount: Long,
				  @Transient open val instanceStartTime: Int,
				  @Transient open val purgeSeq: Long,
				  @Transient open val updateSeq: String,
				  @Transient open val otherDataSize: Long,
				  @Transient open val activeSize: Long,
				  @Transient open val externalSize: Long,
				  @Transient open val fileSize: Long
)
