package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class MoveResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.MOVE_RESULT.id

    var usercode = ""
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0

    override fun readFrom(buffer: ByteBuilder) {
        this.usercode = buffer.readString(16)
        this.x = buffer.readShort()
        this.y = buffer.readShort()
        this.z = buffer.readShort()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawString(this.usercode)
        buffer.writeRawShort(this.x)
        buffer.writeRawShort(this.y)
        buffer.writeRawShort(this.z)
        return buffer
    }
}