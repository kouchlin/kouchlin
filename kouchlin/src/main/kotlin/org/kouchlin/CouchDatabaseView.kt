package org.kouchlin

import com.github.kittinunf.fuel.httpGet
import org.kouchlin.domain.ViewResult
import org.kouchlin.util.STATUS
import org.kouchlin.util.configureAuthentication
import org.kouchlin.util.configureViewParameters
import org.kouchlin.util.toStatus
import org.kouchlin.util.ETAG_HEADER
import org.kouchlin.util.getHeaderValue
import org.kouchlin.domain.ViewResultRow

class CouchDatabaseView(val db: CouchDatabase, val view: String) {
	val viewUri: String

	init {
		var viewPath = view
		if (view.contains("/")) {
			val slices = view.split("/");
			viewPath = "_design/${slices[0]}/_view/${slices[1]}"
		}
		viewUri = "${db.dbName}/$viewPath"
	}

	inline fun < E, reified T : ViewResult<ViewResultRow<E>>> get(conflicts: Boolean? = null,
											   descending: Boolean? = null,
											   endKey: String? = null,
											   endKeyDocId: String? = null,
											   group: Boolean? = null,
											   groupLevel: Int? = null,
											   includeDocs: Boolean? = null,
											   attachments: Boolean? = null,
											   attEncodingInfo: Boolean? = null,
											   inclusiveEnd: Boolean? = null,
											   key: String? = null,
											   keys: List<String>? = null,
											   limit: Int? = null,
											   reduce: Boolean? = null,
											   skip: Int? = null,
											   sorted: Boolean? = null,
											   stable: Boolean? = null,
											   stale: String? = null, /* ok,update_after,false*/
											   startKey: String? = null,
											   startKeyDocId: String? = null,
											   update: String? = null, /*true,false,lazy*/
											   updateSeq: Boolean? = null
	): Triple<T?, String?, STATUS?> {

		val parameters = configureViewParameters(conflicts = conflicts,
				descending = descending,
				endKey = endKey,
				endKeyDocId = endKeyDocId,
				group = group,
				groupLevel = groupLevel,
				includeDocs = includeDocs,
				attachments = attachments,
				attEncodingInfo = attEncodingInfo,
				inclusiveEnd = inclusiveEnd,
				key = key,
				keys = keys,
				limit = limit,
				reduce = reduce,
				skip = skip,
				sorted = sorted,
				stable = stable,
				stale = stale,
				startKey = startKey,
				startKeyDocId = startKeyDocId,
				update = update,
				updateSeq = updateSeq
		)

		val (_, response, result) = viewUri.httpGet(parameters)
				.configureAuthentication(db.server)
				.responseObject(CouchDB.adapter.deserialize(T::class.java))

		val responseEtag = response.getHeaderValue<String?>(ETAG_HEADER)

		return Triple(result.component1(), responseEtag, response.toStatus())
	}

}