package net.ayataka.marinetooler.utils

import org.apache.http.ConnectionReuseStrategy
import org.apache.http.HttpResponse
import org.apache.http.protocol.HttpContext

class AlwaysConnectionReuseStrategy : ConnectionReuseStrategy {
    override fun keepAlive(response: HttpResponse?, context: HttpContext?): Boolean {
        return true
    }
}