package org.kouchlin.domain

abstract class DBUpdatesResult {
	abstract var dbName: String?
	var type: String? = null
	var seq: String? = null
}

abstract class DBUpdates {
	var results: List<DBUpdatesResult>? = null
	abstract var lastSeq: String?
}