package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class Expansion : PacketData {
    var loc5 = ""
    var loc4 = 0
    var type = 0

    override fun readFrom(buffer: ByteBuilder) {
        loc5 = buffer.readString()
        loc4 = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(loc5)
                .writeInt(loc4)
    }
}