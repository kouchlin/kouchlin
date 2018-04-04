package org.kouchlin.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.util.*

class CustomDateTypeAdapter : TypeAdapter<Date>() {
    override fun read(input: JsonReader?): Date {
        val value = input?.nextString()
        val formatter = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
        return formatter.parse(value)
    }

    override fun write(out: JsonWriter?, value: Date?) {
        val formatted = java.text.SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH).format(value)
        out?.value(formatted)
    }
}