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

package org.kouchlin.gson

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import org.kouchlin.domain.*
import org.kouchlin.gson.domain.*
import org.kouchlin.util.JsonAdapter
import java.io.Reader

class GsonJsonAdapter : JsonAdapter {

    override fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T> = object : ResponseDeserializable<T> {
        override fun deserialize(reader: Reader): T = Gson().fromJson<T>(reader, c)
    }

    override fun <T : Any> deserialize(content: String, c: Class<T>): T = Gson().fromJson<T>(content, c)


    override fun deserializeDBUpdates() = gsonDeserializerOf<GsonDBUpdates>() as ResponseDeserializable<DBUpdates<DBUpdatesResult>>
    override fun deserializeDBInfo() = gsonDeserializerOf<GsonDBInfo>()
    override fun deserializeReplicationResponse() = deserialize<GsonReplicationResponse>(GsonReplicationResponse::class.java)


    override fun <T : Any> deserializeChanges(docType: Class<T>?): ResponseDeserializable<Changes<T>> = object : ResponseDeserializable<Changes<T>> {
        override fun deserialize(reader: Reader): Changes<T> {
            val gson = Gson()
            val changes = gson.fromJson<Changes<T>>(reader, GsonChanges::class.java)
            if (docType != null) {
                changes.results?.map {
                    it.doc = it.doc?.let { doc -> gson.fromJson<T>(gson.toJson(doc), docType) }
                }
            }
            return changes
        }
    }

    override fun deserializeBulkDocsResult(): ResponseDeserializable<List<BulkDocsResult>> = object : ResponseDeserializable<List<BulkDocsResult>> {
        override fun deserialize(reader: Reader): List<BulkDocsResult> {
            val gson = Gson()
            return gson.fromJson<List<BulkDocsResult>>(reader, object : TypeToken<List<BulkDocsResult>>() {}.type)
        }
    }

    override fun <V, T> deserializeViewResults(resultType: Class<V>?, docType: Class<T>?):
            ResponseDeserializable<ViewResult<ViewResultRow<V, T>>> = object : ResponseDeserializable<ViewResult<ViewResultRow<V, T>>> {
        override fun deserialize(reader: Reader): ViewResult<ViewResultRow<V, T>> {
            val gson = Gson()
            val result = gson.fromJson<ViewResult<ViewResultRow<V, T>>>(reader, object : TypeToken<GsonViewResult<ViewResultRow<V, T>>>() {}.type)
            if (resultType != null) {
                result.rows.map {
                    it.value = it.value?.let { value -> gson.fromJson<V>(gson.toJson(value), resultType) }
                }
            }
            if (docType != null) {
                result.rows.map {
                    it.doc = it.doc?.let { doc -> gson.fromJson<T>(gson.toJson(doc), docType) }
                }
            }
            return result
        }
    }

    override fun serialize(entity: Any): String = Gson().toJson(entity)

    override fun <T : Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean?): String {
        val gsonBulkDocs = GsonBulkDocs<T>().apply {
            this.docs = docs
            this.newEdits = newEdits
        }

        return serialize(gsonBulkDocs)
    }

    override fun serializeReplicationRequest(cancel: Boolean?,
                                             continuous: Boolean?,
                                             createTarget: Boolean?,
                                             docIds: List<String>?,
                                             filter: String?,
                                             proxy: String?,
                                             source: String?,
                                             target: String?): String {

        val request = GsonReplicationRequest().apply {
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

    override fun findDocumentIdRev(document: Any): Triple<String?, String?, String?> {
        val jsonDocument = when (document) {
            is String -> Gson().fromJson(document, JsonElement::class.java).asJsonObject
            else -> Gson().toJsonTree(document).asJsonObject
        }
        return Triple(jsonDocument.getAsJsonPrimitive("_id")?.asString,
                jsonDocument.getAsJsonPrimitive("_rev")?.asString,
                jsonDocument.toString())
    }

    override fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String {
        val jsonDocument = when (document) {
            is String -> Gson().fromJson(document, JsonElement::class.java).asJsonObject
            else -> Gson().toJsonTree(document).asJsonObject
        }

        id?.let({ jsonDocument.addProperty("_id", it) })
        rev?.let({ jsonDocument.addProperty("_rev", it) })

        return jsonDocument.toString()
    }

    override fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?> {

        val jsonDocument = when (document) {
            is String -> Gson().fromJson(document, JsonElement::class.java).asJsonObject
            else -> Gson().toJsonTree(document).asJsonObject
        }
        val id = jsonDocument.getAsJsonPrimitive("_id")?.asString
        val rev = jsonDocument.getAsJsonPrimitive("_rev")?.asString

        with(jsonDocument) {
            remove("_id")
            remove("_rev")
        }

        return Triple(id, rev, jsonDocument.toString())
    }

}