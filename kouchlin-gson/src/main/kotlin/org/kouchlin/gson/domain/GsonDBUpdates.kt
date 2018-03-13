package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBUpdatesResult


data class GsonDBUpdatesResult(override @SerializedName("db_name") val dbName: String,
							   override val type: String,
							   override val seq: String) : DBUpdatesResult(dbName, type, seq) {}

data class GsonDBUpdates(override val results: List<GsonDBUpdatesResult>,
						 @SerializedName("last_seq") override val lastSeq: String) : DBUpdates(results, lastSeq) {}