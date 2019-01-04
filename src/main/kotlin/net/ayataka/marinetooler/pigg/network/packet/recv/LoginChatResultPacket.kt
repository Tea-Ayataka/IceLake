package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class LoginChatResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.LOGIN_CHAT_RESULT.id

    var success = false // NOT USED
    var serverType: Byte = 0 // NOT USED

    override fun readFrom(buffer: ByteBuilder) {
        success = buffer.readBoolean()
        serverType = buffer.readByte()

        dump("LoginChatResult: success: $success, serverType: $serverType")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}