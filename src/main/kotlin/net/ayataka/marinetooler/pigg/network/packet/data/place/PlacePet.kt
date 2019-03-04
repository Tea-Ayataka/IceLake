package net.ayataka.marinetooler.pigg.network.packet.data.place

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class PlacePet : PacketData {
    var petId = 0
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0
    var direction: Byte = 0
    var sleeping = false

    override fun readFrom(buffer: ByteBuilder) {
        petId = buffer.readInt()
        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
        direction = buffer.readByte()
        sleeping = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeInt(petId)
                .writeShort(x, y, z)
                .writeByte(direction)
                .writeBoolean(sleeping)
    }
}