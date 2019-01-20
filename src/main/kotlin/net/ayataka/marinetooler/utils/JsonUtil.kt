package net.ayataka.marinetooler.utils

import com.google.gson.JsonObject

fun json(block: JsonBuilder.() -> Unit) = JsonBuilder().apply(block).json.toString()

class JsonBuilder {
    val json = JsonObject()

    infix fun String.to(value: String) {
        json.addProperty(this, value)
    }
}