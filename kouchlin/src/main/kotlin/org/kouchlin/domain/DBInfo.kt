package org.kouchlin.domain

data class Cluster(val n: Int, val q: Int, val r: Int, val w: Int)

abstract class DBInfo {
	var cluster: Cluster? = null
	abstract var compactRunning: Boolean?
	abstract var dataSize: Long?
	abstract var dbName: String?
	abstract var diskFormatVersion: Int?
	abstract var diskSize: Long?
	abstract var docCount: Long?
	abstract var docDelCount: Long?
	abstract var instanceStartTime: Long?
	abstract var purgeSeq: Long?
	abstract var updateSeq: String?
	abstract var otherDataSize: Long?
	abstract var activeSize: Long?
	abstract var externalSize: Long?
	abstract var fileSize: Long?
}
