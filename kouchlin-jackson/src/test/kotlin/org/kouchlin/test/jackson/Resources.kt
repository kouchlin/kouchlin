package org.kouchlin.test.jackson

import com.fasterxml.jackson.annotation.JsonProperty

data class DummyJson (@JsonProperty("_id") val id:String?, @JsonProperty("_rev") val rev:String?, val aprop:String )
