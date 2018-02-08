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
        this.x = buffer.readShort()
        this.y = buffer.readShort()
        this.z = buffer.readShort()
        this.dir = buffer.readByte()

        dump("MOVE END X: ${this.x} Y: ${this.y} Z: ${this.z} DIR: ${this.dir}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawShort(this.x)
        buffer.writeRawShort(this.y)
        buffer.writeRawShort(this.z)
        buffer.writeRawByte(this.dir)
        return buffer
    }
}