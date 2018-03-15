package org.kouchlin.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBInfo

interface JsonAdapter {
	fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T>
	fun <T:Any> deserialize(content:String,c: Class<T>): T 
	fun deserializeDBUpdates(): ResponseDeserializable<DBUpdates>
	fun deserializeDBInfo(): ResponseDeserializable<DBInfo>
	fun serialize(entity: Any): String
	fun findDocumentIdRev(document: Any): Triple<String?, String?,String?>
	fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String
	fun deleteDocumentIdRev(document: Any): Triple<String?, String?,String?>
}