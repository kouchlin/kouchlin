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
import com.fasterxml.jackson.databind.node.ObjectNode

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

	override fun <T : Any> deserialize(content: String, c: Class<T>): T = mapper.readValue(content, c)

	override fun deserializeDBUpdates() = jacksonDeserializerOf<JacksonDBUpdates>()
	override fun deserializeDBInfo() = jacksonDeserializerOf<JacksonDBInfo>()

	override fun serialize(entity: Any): String = mapper.writeValueAsString(entity)


	override fun findDocumentIdRev(document: Any): Triple<String?, String?, String?> {

		val jsonNode: JsonNode = mapper.valueToTree(document);
		val id = jsonNode.get("_id")?.asText()
		val rev = jsonNode.get("_rev")?.asText()
		return Triple(id, rev, mapper.writeValueAsString(jsonNode))
	}

	override fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String {
		val jsonNode: JsonNode = mapper.valueToTree(document);
		assert(jsonNode.isObject)
		id?.let { (jsonNode as ObjectNode).put("_id", id) }
		rev?.let { (jsonNode as ObjectNode).put("_rev", rev) }
		return mapper.writeValueAsString(jsonNode)
	}

	override fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?> {
		val jsonNode: JsonNode = mapper.valueToTree(document);
		assert(jsonNode.isObject)
		val id = jsonNode.get("_id")?.asText()
		val rev = jsonNode.get("_rev")?.asText()
		(jsonNode as ObjectNode).remove("_id")
		jsonNode.remove("_id")
		return Triple(id, rev, mapper.writeValueAsString(jsonNode))
	}
}
