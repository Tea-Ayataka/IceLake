package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class RemoveFurniture : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.REMOVE_FURNITURE.id
    override val encrypted = true

    var sequence = 0

    override fun readFrom(buffer: ByteBuilder) {
        sequence = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(sequence)
        return buffer
    }
}