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
    this.removeHeaders(HTTP.CONTENT_LEN)
    this.removeHeaders(HTTP.TRANSFER_ENCODING)
    this.removeHeaders(HTTP.CONN_DIRECTIVE)
    this.removeHeaders(HTTP.CONN_KEEP_ALIVE)
    this.removeHeaders("Proxy-Authenticate")
    this.removeHeaders("Trailers")
    this.removeHeaders("Upgrade")
    this.removeHeaders("TE")
}

fun HttpResponse.removeProxyHeaders() {
    this.removeHeaders(HTTP.CONTENT_LEN)
    this.removeHeaders(HTTP.TRANSFER_ENCODING)
    this.removeHeaders(HTTP.CONN_DIRECTIVE)
    this.removeHeaders(HTTP.CONN_KEEP_ALIVE)
    this.removeHeaders("Trailers")
    this.removeHeaders("Upgrade")
    this.removeHeaders("TE")
}