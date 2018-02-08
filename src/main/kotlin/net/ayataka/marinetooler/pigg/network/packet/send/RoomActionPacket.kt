package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class RoomActionPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ROOM_ACTION.id
    override val encrypted = true

    var actionCode = ""
    var data = ByteArray(0)
    var isAdminRequest = false

    override fun readFrom(buffer: ByteBuilder) {
        this.actionCode = buffer.readString()
        val length = buffer.readShort()

        if (length > 0) {
            this.data = buffer.readRawBytes(length.toInt())
        }

        this.isAdminRequest = buffer.readBoolean()

        info(" ROOM ACTION ID IS ${this.actionCode}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.actionCode)

        if (this.data.isEmpty()) {
            buffer.writeRawShort(-1)
        } else {
            buffer.writeBytes(this.data)
        }

        buffer.writeBoolean(this.isAdminRequest)
        return buffer
    }
}