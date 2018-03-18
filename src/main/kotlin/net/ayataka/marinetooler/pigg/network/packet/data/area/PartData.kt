package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class PartData(val param2: Boolean) {
    var height: Int = 0
    var walkable = false
    var sittable = false
    var orgSittable = false
    var attachable = false
    var attachDirection: Byte? = -1
    var rx: Byte = 0
    var ry: Byte = 0
    var index: Int = 0
    var facing: Facing = Facing.walls[0]
    var rawfacing = 0.0

    fun readFrom(buffer: ByteBuilder) {
        height = buffer.readInt()
        attachable = buffer.readBoolean()
        sittable = buffer.readBoolean()
        walkable = buffer.readBoolean()

        val facing = buffer.readByte()

        rawfacing = facing.toDouble()
        facing = Facing.walls[rawfacing.toInt()]

        if(param2){
            attachDirection = buffer.readByte()
        }

        rx = buffer.readByte()
        ry = buffer.readByte()

        //必要ある気がしない
        orgSittable = sittable
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder {
        val packet = buffer
                .writeRawInt(height)
                .writeBoolean(attachable)
                .writeBoolean(sittable)
                .writeBoolean(walkable)

                .writeRawByte(rawfacing.toByte())
                .writeBoolean(param2)

        attachDirection?.let { packet.writeRawByte(it) }

        packet
                .writeRawByte(rx)
                .writeRawByte(ry)

        return packet
    }

    fun clone() : PartData {
        return this
    }
}