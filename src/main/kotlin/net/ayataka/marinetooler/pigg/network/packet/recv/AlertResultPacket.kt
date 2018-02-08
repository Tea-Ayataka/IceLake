package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class AlertResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ALERT_RESULT.id

    var message = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.message = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.message)
        return buffer
    }
}