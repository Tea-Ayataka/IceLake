package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.toHexString
import java.util.*

class LoginChatPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.LOGIN_CHAT.id
    override val encrypted = true

    var userCode = ""
    var secure: ByteArray = byteArrayOf()
    var connectionId = 0
    var data: ByteArray? = null

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString()
        secure = buffer.readBytes(8)
        connectionId = buffer.readInt()

        if (buffer.array().size - buffer.getPos() > 0) {
            data = buffer.readBytes(buffer.array().size - buffer.getPos())
        }

        dump("LoginChatPacket userCode: $userCode, secure: ${secure.toHexString()}, connectionId: $connectionId")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(userCode)
        buffer.writeRawBytes(secure)
        buffer.writeInt(connectionId)
        data?.let { buffer.writeRawBytes(it) }
        return buffer
    }

    override fun toString(): String {
        return "LoginChatPacket(server=$server, packetId=$packetId, encrypted=$encrypted, userCode='$userCode', secure=${Arrays.toString(secure)}, connectionId=$connectionId)"
    }
}