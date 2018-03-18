package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class TalkPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.TALK.id
    override val encrypted = true

    var text = ""
    var color = 0xFFFFFF
    var balloonColor = 0xFFFFFF

    override fun readFrom(buffer: ByteBuilder) {
        text = buffer.readString()
        color = buffer.readInt()
        balloonColor = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(text)
        buffer.writeRawInt(color)
        buffer.writeRawInt(balloonColor)
        return buffer
    }
}