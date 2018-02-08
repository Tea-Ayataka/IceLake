package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ActionResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ACTION_RESULT.id

    var usercode = ""
    var actionCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.usercode = buffer.readString()
        this.actionCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.usercode)
        buffer.writeString(this.actionCode)
        return buffer
    }
}