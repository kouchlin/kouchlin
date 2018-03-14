package org.kouchlin.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBInfo
import java.io.Reader

class DummyJsonAdapter : JsonAdapter {
	override fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T> = object : ResponseDeserializable<T> {
		override fun deserialize(reader: Reader): T {
			throw UnsupportedOperationException()
		}
	}

	override fun deserializeDBUpdates() = deserialize(DBUpdates::class.java)
	override fun deserializeDBInfo() = deserialize(DBInfo::class.java)

	override fun serialize(entity: Any): String {
		throw UnsupportedOperationException()
	}
	
	override fun findDocumentId(document: Any): Pair<String?, String?> {
		throw UnsupportedOperationException()
	}

}