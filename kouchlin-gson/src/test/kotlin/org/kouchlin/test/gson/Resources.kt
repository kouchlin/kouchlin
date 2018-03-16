package org.kouchlin.test.gson

import com.google.gson.annotations.SerializedName


data class DummyJson (@SerializedName("_id") val id:String?=null, @SerializedName("_rev") val rev:String?=null, val aprop:String )
