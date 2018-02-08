package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString

class EnterRoomPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ENTER_ROOM.id
    override val encrypted = true

    var category = ""
    var code = ""
    var queue = false
    var fromMove = 0

    override fun readFrom(buffer: ByteBuilder) {
        this.category = buffer.readString()
        this.code = buffer.readString()
        this.queue = buffer.readBoolean()
        this.fromMove = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.category)
        buffer.writeString(this.code)
        buffer.writeBoolean(this.queue)
        buffer.writeRawInt(this.fromMove)
        buffer.writeDoubleTimeStamp()
        return buffer
    }
}