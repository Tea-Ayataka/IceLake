package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class PetMoveEnd : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.PET_MOVE_END.id

    var petId = 0
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0
    var dir: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        petId = buffer.readInt()

        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
        dir = buffer.readByte()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(petId)

        buffer.writeShort(x)
        buffer.writeShort(y)
        buffer.writeShort(z)
        buffer.writeByte(dir)

        return buffer
    }
}