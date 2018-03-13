package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBUpdatesResult

data class JacksonDBUpdatesResult(override @JsonProperty("db_name") val dbName: String,
								  override val type: String,
								  override val seq: String) : DBUpdatesResult(dbName, type, seq) {}

data class JacksonDBUpdates(override val results: List<JacksonDBUpdatesResult>,
							@JsonProperty("last_seq") override val lastSeq: String) : DBUpdates(results, lastSeq) {}

