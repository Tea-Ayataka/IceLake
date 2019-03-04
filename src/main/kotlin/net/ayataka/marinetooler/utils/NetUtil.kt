package net.ayataka.marinetooler.utils

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.net.ServerSocket


fun getUnusedPort(): Int {
    val socket = ServerSocket(0)
    val port = socket.localPort
    socket.close()
    return port
}

fun OkHttpClient.get(url: String, block: (Request.Builder.() -> Unit)? = null): String {
    val request = Request.Builder().url(url)
    if (block != null) {
        request.apply(block)
    }
    return newCall(request.build()).execute().use { response -> response.body()!!.string() }
}

fun OkHttpClient.post(url: String, data: String, block: (Request.Builder.() -> Unit)? = null): String {
    val request = Request.Builder().url(url).post(RequestBody.create(MediaType.get("application/x-www-form-urlencoded"), data))
    if (block != null) {
        request.apply(block)
    }
    return newCall(request.build()).execute().use { response -> response.body()!!.string() }
}

fun OkHttpClient.postJson(url: String, data: String, block: (Request.Builder.() -> Unit)? = null): String {
    val request = Request.Builder().url(url).post(RequestBody.create(MediaType.get("application/json"), data))
    if (block != null) {
        request.apply(block)
    }
    return newCall(request.build()).execute().use { response -> response.body()!!.string() }
}