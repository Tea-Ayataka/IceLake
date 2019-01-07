package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class MoveEndResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.MOVE_END_RESULT.id

    var usercode = ""
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0
    var dir: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString(16)
        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
        dir = buffer.readByte()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawString(usercode)
                .writeShort(x, y, z)
                .writeByte(dir)

        return buffer
    }
}