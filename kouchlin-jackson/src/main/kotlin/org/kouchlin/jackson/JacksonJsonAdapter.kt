/*
 * Copyright 2018 Kouchlin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kouchlin.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.ObjectNode
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.jackson.jacksonDeserializerOf
import com.github.kittinunf.fuel.jackson.mapper
import org.kouchlin.domain.*
import org.kouchlin.gson.domain.JacksonChanges
import org.kouchlin.jackson.domain.*
import org.kouchlin.util.JsonAdapter
import java.io.InputStream
import java.io.Reader

class JacksonJsonAdapter : JsonAdapter {
    override fun serializeReplicationRequest(cancel: Boolean?,
                                             continuous: Boolean?,
                                             createTarget: Boolean?,
                                             docIds: List<String>?,
                                             filter: String?,
                                             proxy: String?,
                                             source: String?,
                                             target: String?): String {
        val request = JacksonReplicationRequest().apply {
            this.cancel = cancel
            this.createTarget = createTarget
            this.docIds = docIds
            this.filter = filter
            this.proxy = proxy
            this.source = source
            this.target = target
        }
        return serialize(request)
    }

    override fun deserializeReplicationResponse() = jacksonDeserializerOf<JacksonReplicationResponse>()

    init {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
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

    override fun deserializeDBUpdates() = jacksonDeserializerOf<JacksonDBUpdates>() as ResponseDeserializable<DBUpdates<DBUpdatesResult>>
    override fun deserializeDBInfo() = jacksonDeserializerOf<JacksonDBInfo>()

    override fun <T : Any> deserializeChanges(docType: Class<T>?): ResponseDeserializable<Changes<T>> = object : ResponseDeserializable<Changes<T>> {
        override fun deserialize(reader: Reader): Changes<T>? {
            val changesResultType = mapper.typeFactory.constructParametricType(JacksonChanges::class.java, docType)
            return mapper.readValue<JacksonChanges<T>>(reader, changesResultType)
        }
    }

    override fun deserializeBulkDocsResult(): ResponseDeserializable<List<BulkDocsResult>> = object : ResponseDeserializable<List<BulkDocsResult>> {
        override fun deserialize(reader: Reader): List<BulkDocsResult>? {
            val changesResultType = mapper.typeFactory.constructParametricType(List::class.java, BulkDocsResult::class.java)
            return mapper.readValue<List<BulkDocsResult>>(reader, changesResultType)
        }
    }

    override fun <V, T> deserializeViewResults(resultType: Class<V>?, docType: Class<T>?):
            ResponseDeserializable<ViewResult<ViewResultRow<V, T>>> = object : ResponseDeserializable<ViewResult<ViewResultRow<V, T>>> {
        override fun deserialize(reader: Reader): ViewResult<ViewResultRow<V, T>> {
            val viewResultRowType = mapper.typeFactory.constructParametricType(ViewResultRow::class.java, resultType, docType)
            val viewResultType = mapper.typeFactory.constructParametricType(JacksonViewResult::class.java, viewResultRowType)
            return mapper.readValue<ViewResult<ViewResultRow<V, T>>>(reader, viewResultType)
        }
    }

    override fun serialize(entity: Any): String = mapper.writeValueAsString(entity)
    override fun <T : Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean?): String {
        val bulkDocs = JacksonBulkDocs<T>()
        bulkDocs.docs = docs
        bulkDocs.newEdits = newEdits
        return serialize(bulkDocs)
    }

    override fun findDocumentIdRev(document: Any): Triple<String?, String?, String?> {

        val jsonNode: JsonNode = mapper.valueToTree(document)

        val id = extractValueFromNode(jsonNode.get("_id"))
        val rev = extractValueFromNode(jsonNode.get("_rev"))

        return Triple(id, rev, mapper.writeValueAsString(jsonNode))
    }

    override fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String {
        val jsonNode: JsonNode = mapper.valueToTree(document)
        assert(jsonNode.isObject)
        id?.let { (jsonNode as ObjectNode).put("_id", id) }
        rev?.let { (jsonNode as ObjectNode).put("_rev", rev) }
        return mapper.writeValueAsString(jsonNode)
    }

    override fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?> {
        val jsonNode: JsonNode = when (document) {
            is String -> mapper.readTree(document)
            else -> mapper.valueToTree(document)
        }

        assert(jsonNode.isObject)

        val id = extractValueFromNode(jsonNode.get("_id"))
        val rev = extractValueFromNode(jsonNode.get("_rev"))

        (jsonNode as ObjectNode).remove("_id")
        jsonNode.remove("_rev")
        return Triple(id, rev, mapper.writeValueAsString(jsonNode))
    }

    private fun extractValueFromNode(node: JsonNode?): String? {
        if (node == null) return null

        return when (node) {
            is NullNode -> null
            else -> node.asText()
        }
    }
}