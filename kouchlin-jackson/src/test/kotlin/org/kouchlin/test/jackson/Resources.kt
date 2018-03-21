package org.kouchlin.test.jackson

import com.fasterxml.jackson.annotation.JsonProperty

data class DummyJson (@JsonProperty("_id") val id:String?=null, @JsonProperty("_rev") val rev:String?=null, val foo:String )
