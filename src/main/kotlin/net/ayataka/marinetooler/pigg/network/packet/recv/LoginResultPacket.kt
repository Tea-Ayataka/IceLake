package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.IceLakeApi
import net.ayataka.marinetooler.utils.post
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
                    IceLakeApi.notifyLogin(amebaId, nickname)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }.start()
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}