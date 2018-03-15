package org.kouchlin.test.gson

import com.google.gson.annotations.SerializedName


data class DummyJson (@SerializedName("_id") val id:String?, @SerializedName("_rev") val rev:String?, val aprop:String )
