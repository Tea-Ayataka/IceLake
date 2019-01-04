package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class EnterRoomPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ENTER_ROOM.id
    override val encrypted = true

    var category = ""
    var code = ""
    var queue = false
    var fromMove = 0

    override fun readFrom(buffer: ByteBuilder) {
        category = buffer.readString()
        code = buffer.readString()
        queue = buffer.readBoolean()
        fromMove = buffer.readInt()

        dump("EnterRoomPacket category: $category, code: $code, queue: $queue, fromMove: $fromMove")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(category)
        buffer.writeString(code)
        buffer.writeBoolean(queue)
        buffer.writeInt(fromMove)
        buffer.writeDoubleTimeStamp()
        return buffer
    }
}