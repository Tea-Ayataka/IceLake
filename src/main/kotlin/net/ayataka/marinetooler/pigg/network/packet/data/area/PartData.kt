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
        this.height = buffer.readInt()
        this.attachable = buffer.readBoolean()
        this.sittable = buffer.readBoolean()
        this.walkable = buffer.readBoolean()

        val facing = buffer.readByte()

        this.rawfacing = facing.toDouble()
        this.facing = Facing.walls[this.rawfacing.toInt()]

        if(param2){
            this.attachDirection = buffer.readByte()
        }

        this.rx = buffer.readByte()
        this.ry = buffer.readByte()

        //必要ある気がしない
        this.orgSittable = this.sittable
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder {
        val packet = buffer
                .writeRawInt(this.height)
                .writeBoolean(this.attachable)
                .writeBoolean(this.sittable)
                .writeBoolean(this.walkable)

                .writeRawByte(this.rawfacing.toByte())
                .writeBoolean(this.param2)

        this.attachDirection?.let { packet.writeRawByte(it) }

        packet
                .writeRawByte(this.rx)
                .writeRawByte(this.ry)

        return packet
    }

    fun clone() : PartData {
        return this
    }
}