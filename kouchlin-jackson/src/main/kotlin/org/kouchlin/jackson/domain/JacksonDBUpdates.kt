package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBUpdatesResult

class JacksonDBUpdatesResult : DBUpdatesResult() {
	override @JsonProperty("db_name") var dbName: String? = null
}

class JacksonDBUpdates : DBUpdates() {
	@JsonProperty("last_seq") override var lastSeq: String? = null
}
