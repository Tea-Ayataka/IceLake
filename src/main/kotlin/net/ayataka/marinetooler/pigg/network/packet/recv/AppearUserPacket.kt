package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class AppearUserPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.APPEAR_USER.id

    var usercode = ""

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        // Not implemented
        return null
    }
}