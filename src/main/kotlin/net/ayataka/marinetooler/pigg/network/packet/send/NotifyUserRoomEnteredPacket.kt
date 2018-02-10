package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class NotifyUserRoomEnteredPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.NOTIFY_USER_ROOM_ENTERED.id

    var userCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.userCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.userCode)
        return buffer
    }
}