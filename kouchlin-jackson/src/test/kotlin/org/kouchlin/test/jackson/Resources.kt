package org.kouchlin.test.jackson

import com.fasterxml.jackson.annotation.JsonProperty

data class DummyJson (@JsonProperty("_id") val id:String?=null,
                      @JsonProperty("_rev") val rev:String?=null,
                      val foo:String )

fun dummyJsonFactory(id: String?, rev: String?, foo: String) = DummyJson(id, rev, foo)

fun rev(obj : DummyJson?) = obj?.rev