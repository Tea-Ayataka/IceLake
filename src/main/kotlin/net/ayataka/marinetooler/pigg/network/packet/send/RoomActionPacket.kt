package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString

class RoomActionPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ROOM_ACTION.id
    override val encrypted = true

    var actionCode = ""
    var data = ByteArray(0)
    var isAdminRequest = false

    override fun readFrom(buffer: ByteBuilder) {
        actionCode = buffer.readString()
        val length = buffer.readShort()

        if (length > 0) {
            data = buffer.readBytes(length.toInt())
        }

        isAdminRequest = buffer.readBoolean()

        info(" ROOM ACTION ID IS ${actionCode}")
        info(" AND DATA IS ${data.toHexString()} [${String(data)}]")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(actionCode)

        if (data.isEmpty()) {
            buffer.writeShort(-1)
        } else {
            buffer.writeBytes(data)
        }

        buffer.writeBoolean(isAdminRequest)
        return buffer
    }
}