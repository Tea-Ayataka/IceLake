package net.ayataka.marinetooler.pigg.network.packet.data.place

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class PlacePet {
    var petId = 0
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0
    var direction: Byte = 0
    var sleeping = false

    fun readFrom(buffer: ByteBuilder){
        petId = buffer.readInt()
        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
        direction = buffer.readByte()
        sleeping = buffer.readBoolean()
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeInt(petId)
                .writeShort(x)
                .writeShort(y)
                .writeShort(z)
                .writeByte(direction)
                .writeBoolean(sleeping)
    }
}