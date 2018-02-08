package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class LoginChatResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.LOGIN_CHAT_RESULT.id

    override fun readFrom(buffer: ByteBuilder) {
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}