package org.kouchlin.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import com.github.kittinunf.fuel.jackson.mapper
import org.kouchlin.jackson.domain.JacksonDBInfo
import org.kouchlin.jackson.domain.JacksonDBUpdates
import org.kouchlin.util.JsonAdapter
import java.io.InputStream
import java.io.Reader
import org.kouchlin.jackson.domain.JacksonBulkDocs

class JacksonJsonAdapter : JsonAdapter {

    init {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

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
    override fun <T:Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean?): String = serialize(JacksonBulkDocs(docs,newEdits))
    override fun findDocumentIdRev(document: Any): Triple<String?, String?, String?> {

        val jsonNode: JsonNode = mapper.valueToTree(document);

        val id = extractValueFromNode(jsonNode.get("_id"))
        val rev = extractValueFromNode(jsonNode.get("_rev"))

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
        val jsonNode: JsonNode = when (document) {
            is String -> mapper.readTree(document)
            else -> mapper.valueToTree(document);
        }

        assert(jsonNode.isObject)

        val id = extractValueFromNode(jsonNode.get("_id"))
        val rev = extractValueFromNode(jsonNode.get("_rev"))

        (jsonNode as ObjectNode).remove("_id")
        jsonNode.remove("_rev")
        return Triple(id, rev, mapper.writeValueAsString(jsonNode))
    }

    private fun extractValueFromNode(node: JsonNode?): String? {
        if (node == null) return null;
        return when (node) {
            is NullNode -> null
            else -> node.asText()
        }
    }
}