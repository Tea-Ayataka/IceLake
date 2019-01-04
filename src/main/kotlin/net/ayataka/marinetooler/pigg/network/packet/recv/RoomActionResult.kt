package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class RoomActionResult : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ROOM_ACTION_RESULT.id

    var userCode = ""
    var actionCode = ""
    var data: ByteArray = ByteArray(0)

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString()
        actionCode = buffer.readString()

        data = buffer.readAllBytes()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(userCode)
        buffer.writeString(actionCode)
        buffer.writeRawBytes(data)

        return buffer
    }
}