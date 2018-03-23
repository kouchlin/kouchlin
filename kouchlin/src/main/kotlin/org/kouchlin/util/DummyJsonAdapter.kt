package org.kouchlin.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.kouchlin.domain.Changes
import org.kouchlin.domain.DBInfo
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.ViewResult
import org.kouchlin.domain.ViewResultRow
import java.io.Reader

class DummyJsonAdapter : JsonAdapter {
	override fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T> = object : ResponseDeserializable<T> {
		override fun deserialize(reader: Reader): T {
			throw UnsupportedOperationException()
		}
	}

	override fun <T : Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean?): String {
		throw UnsupportedOperationException()
	}

	override fun <T : Any> deserialize(content: String, c: Class<T>): T {
		throw UnsupportedOperationException()
	}

	override fun deserializeDBUpdates() = deserialize(DBUpdates::class.java)
	override fun deserializeDBInfo() = deserialize(DBInfo::class.java)
	@Suppress("UNCHECKED_CAST")
	override fun <T : Any> deserializeChanges(docType: Class<T>?) = deserialize(Changes::class.java as Class<Changes<T>>)

	@Suppress("UNCHECKED_CAST")
	override fun <V, T> deserializeViewResults(resultType: Class<V>?, docType: Class<T>?):
			ResponseDeserializable<ViewResult<ViewResultRow<V, T>>> = deserialize(ViewResult::class.java as Class<ViewResult<ViewResultRow<V, T>>>)

	override fun serialize(entity: Any): String {
		throw UnsupportedOperationException()
	}

	override fun findDocumentIdRev(document: Any): Triple<String?, String?, String?> {
		throw UnsupportedOperationException()
	}

	override fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String {
		throw UnsupportedOperationException()
	}

	override fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?> {
		throw UnsupportedOperationException()
	}
}