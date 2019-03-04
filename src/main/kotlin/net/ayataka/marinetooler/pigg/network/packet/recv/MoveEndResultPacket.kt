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
    var direction: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString(16)
        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
        direction = buffer.readByte()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawString(usercode)
                .writeShort(x, y, z)
                .writeByte(direction)

        return buffer
    }

    override fun toString(): String {
        return "MoveEndResultPacket(server=$server, packetId=$packetId, usercode='$usercode', x=$x, y=$y, z=$z, direction=$direction)"
    }
}