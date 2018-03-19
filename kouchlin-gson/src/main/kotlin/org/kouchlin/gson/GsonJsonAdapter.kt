package org.kouchlin.gson

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.google.gson.Gson
import com.google.gson.JsonElement
import org.kouchlin.gson.domain.GsonBulkDocs
import org.kouchlin.gson.domain.GsonChanges
import org.kouchlin.gson.domain.GsonDBInfo
import org.kouchlin.gson.domain.GsonDBUpdates
import org.kouchlin.util.JsonAdapter
import java.io.Reader
import com.google.gson.reflect.TypeToken

class GsonJsonAdapter : JsonAdapter {
	override fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T> = object : ResponseDeserializable<T> {
		override fun deserialize(reader: Reader): T = Gson().fromJson<T>(reader, c)
	}

	override fun <T : Any> deserialize(content: String, c: Class<T>): T = Gson().fromJson<T>(content, c)

	override fun serialize(entity: Any): String = Gson().toJson(entity)

	override fun <T : Any> serializeBulkDocs(docs: List<T>, newEdits: Boolean?): String {
		val gsonBulkDocs = GsonBulkDocs<T>();
		gsonBulkDocs.docs = docs
		gsonBulkDocs.newEdits = newEdits
		return serialize(gsonBulkDocs)
	}

	override fun deserializeDBUpdates() = gsonDeserializerOf<GsonDBUpdates>()
	override fun deserializeDBInfo() = gsonDeserializerOf<GsonDBInfo>()
	override fun <T : Any> deserializeChanges() = deserialize<GsonChanges<T>>(GsonChanges::class.java as Class<GsonChanges<T>>)

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
		id?.let({ jsonDocument.addProperty("_id", id) })
		rev?.let({ jsonDocument.addProperty("_rev", rev) })

		return jsonDocument.toString()
	}

	override fun deleteDocumentIdRev(document: Any): Triple<String?, String?, String?> {

		val jsonDocument = when (document) {
			is String -> Gson().fromJson(document, JsonElement::class.java).asJsonObject
			else -> Gson().toJsonTree(document).asJsonObject
		}
		val id = jsonDocument.getAsJsonPrimitive("_id")?.asString
		val rev = jsonDocument.getAsJsonPrimitive("_rev")?.asString
		jsonDocument.remove("_id")
		jsonDocument.remove("_rev")
		return Triple(id, rev, jsonDocument.toString())
	}

}