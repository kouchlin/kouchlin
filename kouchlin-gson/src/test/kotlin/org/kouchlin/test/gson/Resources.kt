package org.kouchlin.test.gson

import com.google.gson.annotations.SerializedName

data class DummyJson(@SerializedName("_id") val id: String? = null,
                     @SerializedName("_rev") val rev: String? = null,
                     val foo: String? = null)

fun dummyJsonFactory(id: String?, rev: String?, foo: String) = DummyJson(id, rev, foo)

fun rev(obj: DummyJson?) = obj?.rev