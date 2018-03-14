package org.kouchlin.gson

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.gson.gsonDeserializerOf
import com.google.gson.Gson
import org.kouchlin.gson.domain.GsonDBInfo
import org.kouchlin.gson.domain.GsonDBUpdates
import org.kouchlin.util.JsonAdapter
import java.io.Reader

class GsonJsonAdapter : JsonAdapter {
	override fun <T : Any> deserialize(c: Class<T>): ResponseDeserializable<T> = object : ResponseDeserializable<T> {
		override fun deserialize(reader: Reader): T = Gson().fromJson<T>(reader, c)
	}

	override fun serialize(entity: Any): String = Gson().toJson(entity)


	override fun deserializeDBUpdates() = gsonDeserializerOf<GsonDBUpdates>()
	override fun deserializeDBInfo() = gsonDeserializerOf<GsonDBInfo>()

	override fun findDocumentId(document: Any): Pair<String?, String?> {
		val jsonDocument = Gson().toJsonTree(document).asJsonObject
		return Pair(jsonDocument.getAsJsonPrimitive("_id").asString, jsonDocument.getAsJsonPrimitive("_rev").asString)
	}
	
//	fun setDocumentId(document: Any, id: String?, rev: String?): Any {
//		val jsonDocument = Gson().toJsonTree(document).asJsonObject
//		return Pair(jsonDocument.getAsJsonPrimitive("_id").asString, jsonDocument.getAsJsonPrimitive("_rev").asString)
//	}
}