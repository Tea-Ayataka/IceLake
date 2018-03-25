package net.ayataka.marinetooler.emulator.pigg

import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.send.LoginPacket
import net.ayataka.marinetooler.proxy.websocket.IPacketListener
import net.ayataka.marinetooler.utils.web.WebClient
import java.nio.ByteBuffer

class VirtualPigg(val amebaId: String, val password: String) {
    var connected = false
    var authToken: String? = null

    val protocol = Protocol()

    var info: VClient? = null
    var chat: VClient? = null

    fun login() {
        val web = WebClient()
        val token = Regex("name=\"csrf_token\" value=\"([a-z0-9]+)\"").find(web.get("https://dauth.user.ameba.jp/accounts/login"))?.groupValues?.get(1)
        val result = web.post("https://dauth.user.ameba.jp/accounts/login", "accountId=$amebaId&password=$password&csrf_token=$token")

        // ログインに成功したときはリダイレクトされる
        if (result.isEmpty()) {
            web.get("http://pigg.ameba.jp/core/main")
            println(web.cookie.cookieStore.cookies)

            authToken = web.cookie.cookieStore.cookies.find { it.name == "N" }!!.value

            println(authToken)

            info = VClient("", object : IPacketListener {
                var loggedIn = false
                override fun send(buffer: ByteBuffer): ByteBuffer? {
                    return buffer
                }

                override fun receive(buffer: ByteBuffer): ByteBuffer? {
                    if (!loggedIn) {
                        info!!.send(LoginPacket().write(protocol.cipherKey[ServerType.INFO]))
                        loggedIn = true
                    }

                    protocol.convert(buffer, ServerType.INFO)?.let {

                    }

                    return buffer
                }
            })
        }
    }

    fun connect() {
        //info?.connect()
    }

    fun disconnect() {

    }
}

// Test
fun main(args: Array<String>) {
    val vPigg = VirtualPigg("savmario", "mariolinnku")

    vPigg.login()
    vPigg.connect()
}