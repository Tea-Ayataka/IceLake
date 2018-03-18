package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class MoveFurniture : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.MOVE_FURNITURE.id
    override val encrypted = true

    var sequence = 0
    var x = 0
    var y = 0
    var z = 0
    var direction: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        sequence = buffer.readInt()
        direction = buffer.readByte()
        x = buffer.readInt()
        y = buffer.readInt()
        z = buffer.readInt()

        dump("Sequence: ${sequence}, Direction: ${direction}, Pos: ${x} ${y} ${z}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawInt(sequence)
        buffer.writeRawByte(direction)
        buffer.writeRawInt(x)
        buffer.writeRawInt(y)
        buffer.writeRawInt(z)
        return buffer
    }
}