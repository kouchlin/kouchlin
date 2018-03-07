package org.kouchlin.util

data class SaveResponse(val id: String, val ok: Boolean, val rev: String)
data class Version(val version: String)