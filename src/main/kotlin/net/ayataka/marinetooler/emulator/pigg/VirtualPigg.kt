package net.ayataka.marinetooler.emulator.pigg

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.ayataka.marinetooler.pigg.network.Protocol
import java.net.URL

class VirtualPigg(val amebaId: String, val password: String) {
    var connected = false
    val protocol = Protocol()

    var secureCode: ByteArray? = null
    var info: VClient? = null
    var chat: VClient? = null

    fun login() {
        val result = URL("http://takapon.php.xdomain.jp/pigg/login.php?amebaId=$amebaId&password=$password").readText(Charsets.UTF_8)
        val json = Gson().fromJson(result, JsonObject::class.java)

        val nTicket = json["N"].asString
        val piggTicket = json["pigg"].asString


    }

    fun connect() {
        info?.connect()
    }

    fun disconnect() {

    }
}

// Test
fun main(args: Array<String>) {
    val vPigg = VirtualPigg("savmario", "mariolinnku")
    vPigg.login()
    vPigg.connect()

    Thread {
        while (true) {
        }
    }.start()
}