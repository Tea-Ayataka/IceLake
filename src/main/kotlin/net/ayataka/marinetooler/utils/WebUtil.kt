package net.ayataka.marinetooler.utils

import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.protocol.HTTP
import java.net.MalformedURLException
import java.net.URL

fun validateUrl(urlString: String): Boolean {
    try {
        URL(urlString)
    } catch (ex: MalformedURLException) {
        return false
    }

    return true
}

fun HttpRequest.removeProxyHeaders() {
    removeHeaders(HTTP.CONTENT_LEN)
    removeHeaders(HTTP.TRANSFER_ENCODING)
    removeHeaders(HTTP.CONN_DIRECTIVE)
    removeHeaders(HTTP.CONN_KEEP_ALIVE)
    removeHeaders("Proxy-Authenticate")
    removeHeaders("Trailers")
    removeHeaders("Upgrade")
    removeHeaders("TE")
}

fun HttpResponse.removeProxyHeaders() {
    removeHeaders(HTTP.CONTENT_LEN)
    removeHeaders(HTTP.TRANSFER_ENCODING)
    removeHeaders(HTTP.CONN_DIRECTIVE)
    removeHeaders(HTTP.CONN_KEEP_ALIVE)
    removeHeaders("Trailers")
    removeHeaders("Upgrade")
    removeHeaders("TE")
}