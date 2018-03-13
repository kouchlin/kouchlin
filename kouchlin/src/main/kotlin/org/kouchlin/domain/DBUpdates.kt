package org.kouchlin.domain

open class DBUpdatesResult(@Transient open val dbName:String,
						   @Transient open val type:String,
						   @Transient open val seq: String)

open class DBUpdates(@Transient open val results: List<DBUpdatesResult>, @Transient open val lastSeq:String) {}