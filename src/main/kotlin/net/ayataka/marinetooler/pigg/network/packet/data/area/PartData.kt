package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class PartData(private var hasAttachDirection: Boolean) : PacketData {
    var height: Int = 0
    var walkable = false
    var sittable = false
    var attachable = false
    var attachDirection: Byte? = null
    var rx: Byte = 0
    var ry: Byte = 0
    var index: Int = 0
    var wall: Wall = Wall.NONE // Read only
    var facing: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
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
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeInt(height)
                .writeBoolean(attachable, sittable, walkable)
                .writeByte(facing)

        attachDirection?.let {
            buffer.writeByte(it)
        }

        buffer.writeByte(rx, ry)
    }

    override fun toString(): String {
        return "PartData(hasAttachDirection=$hasAttachDirection, height=$height, walkable=$walkable, sittable=$sittable, attachable=$attachable, attachDirection=$attachDirection, rx=$rx, ry=$ry, index=$index, wall=$wall, facing=$facing)"
    }


}