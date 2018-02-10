package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetNoticeBoardMessageOfAreaPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_NOTICE_BOARD_MESSAGE_OF_AREA.id

    var userCode = ""
    var roomName = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.userCode = buffer.readString()
        this.roomName = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.userCode)
        buffer.writeString(this.roomName)
        return buffer
    }
}