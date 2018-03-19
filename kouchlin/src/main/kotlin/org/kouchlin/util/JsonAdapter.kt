package org.kouchlin.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.kouchlin.domain.Changes
import org.kouchlin.domain.DBInfo
import org.kouchlin.domain.DBUpdates

interface JsonAdapter {
	fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T>
	fun <T : Any> deserialize(content: String, c: Class<T>): T?
	fun <T : Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean? = null): String
	fun deserializeDBUpdates(): ResponseDeserializable<DBUpdates>
	fun deserializeDBInfo(): ResponseDeserializable<DBInfo>
	fun <T : Any> deserializeChanges(): ResponseDeserializable<Changes<T>>
	fun serialize(entity: Any): String
	fun findDocumentIdRev(document: Any): Triple<String?, String?, String?>
	fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String
	fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?>
}