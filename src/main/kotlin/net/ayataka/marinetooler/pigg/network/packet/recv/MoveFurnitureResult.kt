package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.trace

class MoveFurnitureResult : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.MOVE_FURNITURE_RESULT.id

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

        trace("Sequence: ${sequence}, Direction: ${direction}, Pos: ${x} ${y} ${z}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(sequence)
        buffer.writeByte(direction)
        buffer.writeInt(x)
        buffer.writeInt(y)
        buffer.writeInt(z)
        return buffer
    }
}