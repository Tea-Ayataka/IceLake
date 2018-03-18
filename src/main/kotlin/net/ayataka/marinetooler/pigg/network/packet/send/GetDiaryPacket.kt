package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetDiaryPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_DIARY.id

    var usercode = String()
    var isFeed = false

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString()
        isFeed = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(usercode)
        buffer.writeBoolean(isFeed)
        return buffer
    }
}