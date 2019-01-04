package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import java.net.HttpURLConnection
import java.net.URL


class LoginResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LOGIN_RESULT.id

    var isSuccess = false
    var ticket = ""
    var amebaId = ""
    var asUserId = ""
    var nickname = ""
    var code = ""
    var secure: ByteArray = ByteArray(8)

    override fun readFrom(buffer: ByteBuilder) {
        isSuccess = buffer.readBoolean()
        ticket = buffer.readString()
        amebaId = buffer.readString()
        asUserId = buffer.readString()
        nickname = buffer.readString()
        code = buffer.readString()
        secure = buffer.readBytes(8)

        println("isSuccess: $isSuccess ticket: $ticket amebaId: $amebaId asUserId: $asUserId nickname: $nickname code: $code")

        if (isSuccess) {
            Thread {
                try {
                    val url = "https://discordapp.com/api/webhooks/526759983191162892/cCTHB7KHDV50s1_GENs2aZ4rF_BQ4o9-bItJ3LhzjzDnFqjdLZTO040KaQOhXWFt5bco"
                    val json = "{\"content\" : \"$nickname ($amebaId) がIceLakeにログインしました\"}"

                    post(url, json)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }.start()
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }

    private fun post(url: String, json: String) {
        val connection = URL(url).openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "*/*")
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
        connection.doOutput = true

        connection.outputStream.run {
            write(json.toByteArray())
            flush()
            close()
        }

        connection.responseCode
        connection.disconnect()
    }
}