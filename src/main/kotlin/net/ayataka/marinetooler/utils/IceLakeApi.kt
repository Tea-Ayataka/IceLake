package net.ayataka.marinetooler.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.OkHttpClient
import org.apache.commons.text.StringEscapeUtils

object IceLakeApi {
    private const val API_ENDPOINT = "https://icelake.pw/api"
    private val DEFAULT_ACTIONS = arrayOf("joy", "sad", "frustrate")
    private val httpClient = OkHttpClient()
    private val GSON = Gson()

    fun registerAction(code: String, type: String, name: String) {
        if (code in DEFAULT_ACTIONS) {
            return
        }

        info("Registering action: $code ($name)")
        httpClient.postJson("$API_ENDPOINT/actions", json {
            "code" to code
            "type" to type
            "name" to StringEscapeUtils.escapeJson(name)
        })
    }

    fun getActions(): Map<String, String> {
        val json = GSON.fromJson(httpClient.get("$API_ENDPOINT/actions"), JsonObject::class.java)
        return json["actions"].asJsonArray.map { it.asJsonObject["code"].asString to StringEscapeUtils.unescapeJson(it.asJsonObject["name"].asString) }.toMap()
    }

    fun registerArea(code: String, name: String) {
        info("Registering area: $code ($name)")
        httpClient.postJson("$API_ENDPOINT/areas", json {
            "code" to code
            "name" to StringEscapeUtils.escapeJson(name)
        })
    }

    fun getAreas(): Map<String, String> {
        val json = GSON.fromJson(httpClient.get("$API_ENDPOINT/areas"), JsonObject::class.java)
        return json["areas"].asJsonArray.map { it.asJsonObject["code"].asString to StringEscapeUtils.unescapeJson(it.asJsonObject["name"].asString) }.toMap()
    }

    fun registerShop(code: String, name: String) {
        info("Registering shop: $code ($name)")
        httpClient.postJson("$API_ENDPOINT/shops", json {
            "code" to code
            "name" to StringEscapeUtils.escapeJson(name)
        })
    }

    fun getShops(): Map<String, String> {
        val json = GSON.fromJson(httpClient.get("$API_ENDPOINT/shops"), JsonObject::class.java)
        return json["shops"].asJsonArray.map { it.asJsonObject["code"].asString to StringEscapeUtils.unescapeJson(it.asJsonObject["name"].asString) }.toMap()
    }
}