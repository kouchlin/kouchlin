package org.kouchlin.util

import com.github.kittinunf.fuel.core.ResponseDeserializable
import org.kouchlin.domain.DBUpdates
import org.kouchlin.domain.DBInfo

interface DeserializerHelper {
	
	 fun <T:Any > deserialize(c:Class<T>): ResponseDeserializable<T>
	 fun deserializeDBUpdates() : ResponseDeserializable<DBUpdates> 
	 fun deserializeDBInfo() : ResponseDeserializable<DBInfo> 
}