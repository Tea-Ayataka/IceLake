package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class Expansion : PacketData {
    var areaCode = ""
    var index = 0
    var type = 0

    override fun readFrom(buffer: ByteBuilder) {
        areaCode = buffer.readString()
        index = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(areaCode)
                .writeInt(index)
    }
}