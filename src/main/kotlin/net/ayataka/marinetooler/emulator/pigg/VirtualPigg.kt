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
        val token = Regex("name=\"csrf_token\" value=\"([a-z0-9]+)\"").find(web.get("https://dauth.user.ameba.jp/login/ameba"))?.groupValues?.get(1)
        val result = web.post("https://dauth.user.ameba.jp/accounts/login", "accountId=$amebaId&password=$password&csrf_token=$token")

        if (!result.contains("IDまたはパスワードが正しくありません")) {
            //val shit = web.get("https://pigg.ameba.jp")
            //println("cookies : ${web.cookie.cookieStore.cookies}")
            println(result)


            authToken = web.cookie.cookieStore.cookies.find { it.name == "N" }!!.value

            info = VClient("wss://27.133.213.64:443/command", object : IPacketListener {
                var loggedIn = false
                override fun send(buffer: ByteBuffer): ByteBuffer? {
                    return buffer
                }

                override fun receive(buffer: ByteBuffer): ByteBuffer? {
                    protocol.convert(buffer, ServerType.INFO)?.let {

                    }

                    // login
                    if (!loggedIn) {
                        val packet = LoginPacket()
                        packet.ticket = "6b54a1f026f1eaca720807cfec57f1a1bbd9134312a4b277e9d865a53f334041276199165f0cc5f48aa99bbefafde5c2a0d4a19f8b52b560e041496d45468431ea4bf5f518bb3e4cee72d8bfb810ee3c"
                        packet.amebaAuthTicket = "e4d72a18f7a1f2908879fac750ad0e4975d9fe1392d7524ab0ba8ee73b0d1d13257eaa5570abab918e1279eae73b8b0f45395593592946814b7683a8b125e3d0e44dc71bf117d56f681508eb2972cc55bf2404647750d4c380201afe3558bca96452edb1f7dfbbdc74f7e1cba28df222"
                        packet.password = "d41d8cd98f00b204e9800998ecf8427e"

                        packet.fuck()

                        info!!.send(packet.write(protocol.cipherKey[ServerType.INFO]))
                        loggedIn = true
                    }

                    return buffer
                }
            })
        }
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
   // vPigg.connect()

 //   Thread.sleep(10000000)
}