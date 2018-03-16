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
        this.petId = buffer.readInt()
        this.x = buffer.readShort()
        this.y = buffer.readShort()
        this.z = buffer.readShort()
        this.direction = buffer.readByte()
        this.sleeping = buffer.readBoolean()
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeRawInt(this.petId)
                .writeRawShort(this.x)
                .writeRawShort(this.y)
                .writeRawShort(this.z)
                .writeRawByte(this.direction)
                .writeBoolean(this.sleeping)
    }
}