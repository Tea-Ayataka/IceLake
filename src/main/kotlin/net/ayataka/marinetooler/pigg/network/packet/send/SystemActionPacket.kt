package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class SystemActionPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.SYSTEM_ACTION.id
    override val encrypted = true

    var actionCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        actionCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(actionCode)
        return buffer
    }
}