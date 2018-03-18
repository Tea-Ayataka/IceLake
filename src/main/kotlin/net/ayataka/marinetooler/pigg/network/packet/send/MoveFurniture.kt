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
        this.sequence = buffer.readInt()
        this.direction = buffer.readByte()
        this.x = buffer.readInt()
        this.y = buffer.readInt()
        this.z = buffer.readInt()

        dump("Sequence: ${this.sequence}, Direction: ${this.direction}, Pos: ${this.x} ${this.y} ${this.z}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawInt(this.sequence)
        buffer.writeRawByte(this.direction)
        buffer.writeRawInt(this.x)
        buffer.writeRawInt(this.y)
        buffer.writeRawInt(this.z)
        return buffer
    }
}