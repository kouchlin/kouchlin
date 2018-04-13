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

package org.kouchlin.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.kouchlin.domain.*

/**
This interface defines the JSON serialization/deserializarion operations required by CouchDB API.
It allows to decouple Kouchlin from the underline JSON library
 */
interface JsonAdapter {
    fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T>
    fun <T : Any> deserialize(content: String, c: Class<T>): T?
    fun <T : Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean? = null): String
    fun serializeReplicationRequest(cancel: Boolean? = null,
                                    continuous: Boolean? = null,
                                    createTarget: Boolean? = null,
                                    docIds: List<String>? = null,
                                    filter: String? = null,
                                    proxy: String? = null,
                                    source: String? = null,
                                    target: String? = null): String

    fun deserializeDBUpdates(): ResponseDeserializable<DBUpdates<DBUpdatesResult>>
    fun deserializeBulkDocsResult(): ResponseDeserializable<List<BulkDocsResult>>
    fun deserializeDBInfo(): ResponseDeserializable<DBInfo>
    fun deserializeReplicationResponse(): ResponseDeserializable<ReplicationResponse<*>>
    fun <T : Any> deserializeChanges(docType: Class<T>? = null): ResponseDeserializable<Changes<T>>
    fun <V, T> deserializeViewResults(resultType: Class<V>?, docType: Class<T>?): ResponseDeserializable<ViewResult<ViewResultRow<V, T>>>
    fun serialize(entity: Any): String
    fun findDocumentIdRev(document: Any): Triple<String?, String?, String?>
    fun appendDocumentIdRev(document: Any, id: String?, rev: String?): String
    fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?>
}