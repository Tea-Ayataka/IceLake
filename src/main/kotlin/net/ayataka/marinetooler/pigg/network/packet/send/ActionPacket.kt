package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ActionPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ACTION.id
    override val encrypted = true

    var actionId = ""

    override fun readFrom(buffer: ByteBuilder) {
        actionId = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(actionId)
        return buffer
    }
}