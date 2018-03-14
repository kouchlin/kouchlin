package org.kouchlin.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import com.github.kittinunf.fuel.jackson.mapper
import org.kouchlin.jackson.domain.JacksonDBInfo
import org.kouchlin.jackson.domain.JacksonDBUpdates
import org.kouchlin.util.JsonAdapter
import java.io.InputStream
import java.io.Reader

class JacksonJsonAdapter : JsonAdapter {
	override fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T> = object : ResponseDeserializable<T> {
		override fun deserialize(reader: Reader): T? {
			return mapper.readValue(reader, c)
		}

		override fun deserialize(content: String): T? {
			return mapper.readValue(content, c)
		}

		override fun deserialize(bytes: ByteArray): T? {
			return mapper.readValue(bytes, c)
		}

		override fun deserialize(inputStream: InputStream): T? {
			return mapper.readValue(inputStream, c)
		}
	}

	override fun deserializeDBUpdates() = jacksonDeserializerOf<JacksonDBUpdates>()
	override fun deserializeDBInfo() = jacksonDeserializerOf<JacksonDBInfo>()

	override fun serialize(entity: Any): String = mapper.writeValueAsString(entity)
	
	
	override fun findDocumentId(document: Any): Pair<String?, String?> {
		
		val jsonNode : JsonNode = mapper.valueToTree(document);
		val id = jsonNode.get("_id")?.toString()
		val rev = jsonNode.get("_rev")?.toString()
		return Pair(id,rev)
	}
}
