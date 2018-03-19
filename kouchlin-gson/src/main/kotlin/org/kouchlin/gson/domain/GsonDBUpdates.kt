package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBUpdatesResult


class GsonDBUpdatesResult : DBUpdatesResult() {
	override @SerializedName("db_name") var dbName: String? = null
}

class GsonDBUpdates : DBUpdates() { 
	@SerializedName("last_seq") override var lastSeq: String? = null
}
