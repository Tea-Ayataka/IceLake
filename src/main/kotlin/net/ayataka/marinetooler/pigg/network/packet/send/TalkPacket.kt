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
        this.text = buffer.readString()
        this.color = buffer.readInt()
        this.balloonColor = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.text)
        buffer.writeRawInt(this.color)
        buffer.writeRawInt(this.balloonColor)
        return buffer
    }
}