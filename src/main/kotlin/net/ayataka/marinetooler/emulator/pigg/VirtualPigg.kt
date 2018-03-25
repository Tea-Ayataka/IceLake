package net.ayataka.marinetooler.emulator.pigg

import org.apache.http.protocol.HTTP
import java.net.CookieStore

class VirtualPigg(val amebaId: String, val password: String) {
    var connected = false
    var cookie: CookieStore = null

    fun login() {
        val httpGet = HTTP.
    }

    fun connect() {
        if (cookie == null) {
            return
        }

    }

    fun disconnect() {

    }
}