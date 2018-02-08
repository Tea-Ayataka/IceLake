package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class TalkResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.TALK_RESULT.id

    var usercode = ""
    var message = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.usercode = buffer.readString(16)
        this.message = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}