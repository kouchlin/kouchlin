package org.kouchlin.gson.domain

import com.google.gson.annotations.SerializedName
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBUpdatesResult


class GsonDBUpdatesResult : DBUpdatesResult() {
    @SerializedName("db_name") override var dbName: String? = null
}

class GsonDBUpdates : DBUpdates<GsonDBUpdatesResult>() {
	@SerializedName("last_seq") override var lastSeq: String? = null
}
