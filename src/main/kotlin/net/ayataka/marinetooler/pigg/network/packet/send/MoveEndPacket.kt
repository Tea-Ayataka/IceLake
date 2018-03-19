package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class MoveEndPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.MOVE_END.id
    override val encrypted = true

    var x: Short = 0
    var y: Short = 0
    var z: Short = 0
    var dir: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
        dir = buffer.readByte()

        dump("MOVE END X: ${x} Y: ${y} Z: ${z} DIR: ${dir}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeShort(x)
        buffer.writeShort(y)
        buffer.writeShort(z)
        buffer.writeByte(dir)
        return buffer
    }
}