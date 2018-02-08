package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class CancelTypingPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.CANCEL_TYPING.id

    override fun readFrom(buffer: ByteBuilder) {

    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return buffer // EMPTY DATA
    }
}