package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class PartData(private var hasAttachDirection: Boolean) {
    var height: Int = 0
    var walkable = false
    var sittable = false
    var orgSittable = false
    var attachable = false
    var attachDirection: Byte? = -1
    var rx: Byte = 0
    var ry: Byte = 0
    var index: Int = 0
    var wall: Wall = Wall.NONE // Read only
    var facing: Byte = 0

    fun readFrom(buffer: ByteBuilder) {
        height = buffer.readInt()
        attachable = buffer.readBoolean()
        sittable = buffer.readBoolean()
        walkable = buffer.readBoolean()
        facing = buffer.readByte()

        wall = Wall.values()[facing.toInt()]

        if (hasAttachDirection) {
            attachDirection = buffer.readByte()
        }

        rx = buffer.readByte()
        ry = buffer.readByte()

        //必要ある気がしない
        orgSittable = sittable
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder {
        buffer.writeInt(height)
                .writeBoolean(attachable)
                .writeBoolean(sittable)
                .writeBoolean(walkable)
                .writeByte(facing)
                .writeBoolean(hasAttachDirection)

        attachDirection?.let { buffer.writeByte(it) }

        buffer.writeByte(rx)
                .writeByte(ry)

        return buffer
    }
}