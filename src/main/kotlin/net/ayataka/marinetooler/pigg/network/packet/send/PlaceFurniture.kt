package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class PlaceFurniture : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.PLACE_FURNITURE.id
    override val encrypted = true

    var code = ""
    var x = 0
    var y = 0
    var z = 0
    var direction: Byte = 0
    var isFinishTutorial = false

    override fun readFrom(buffer: ByteBuilder) {
        this.code = buffer.readString()
        this.direction = buffer.readByte()
        this.x = buffer.readInt()
        this.y = buffer.readInt()
        this.z = buffer.readInt()
        this.isFinishTutorial = buffer.readBoolean()

        dump("Code: ${this.code}, Direction: ${this.direction}, Pos: ${this.x} ${this.y} ${this.z}, isFinishTutorial: ${this.isFinishTutorial}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.code)
        buffer.writeRawByte(this.direction)
        buffer.writeRawInt(this.x)
        buffer.writeRawInt(this.y)
        buffer.writeRawInt(this.z)
        buffer.writeBoolean(this.isFinishTutorial)
        return buffer
    }
}