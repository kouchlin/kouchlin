package org.kouchlin.domain

import com.google.gson.annotations.SerializedName

data class DBUpdatesResult(@SerializedName("db_name") val dbName:String,
						   val type:String,
						   val seq: String)

data class DBUpdates(val results: List<DBUpdatesResult>, @SerializedName("last_seq") val lastSeq:String) {}