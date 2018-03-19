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
        code = buffer.readString()
        direction = buffer.readByte()
        x = buffer.readInt()
        y = buffer.readInt()
        z = buffer.readInt()
        isFinishTutorial = buffer.readBoolean()

        dump("Code: ${code}, Direction: ${direction}, Pos: ${x} ${y} ${z}, isFinishTutorial: ${isFinishTutorial}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(code)
        buffer.writeByte(direction)
        buffer.writeInt(x)
        buffer.writeInt(y)
        buffer.writeInt(z)
        buffer.writeBoolean(isFinishTutorial)
        return buffer
    }
}