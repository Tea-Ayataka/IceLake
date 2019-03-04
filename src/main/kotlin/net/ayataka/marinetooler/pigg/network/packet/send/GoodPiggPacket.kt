package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GoodPiggPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.GIVE_GOOD.id

    var usercode = ""

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(usercode)
        return buffer
    }
}