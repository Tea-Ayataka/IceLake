package net.ayataka.marinetooler.pigg.network.packet.data.club

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class ClubEmblemData(var isMember: Boolean? = null) : PacketData {
    var symbol = 0
    var base = 0
    var simple = 0
    var baseColor = 0
    var simpleColor = 0

    override fun readFrom(buffer: ByteBuilder) {
        symbol = buffer.readInt()
        base = buffer.readInt()
        baseColor = buffer.readInt()
        simple = buffer.readInt()
        simpleColor = buffer.readInt()
        isMember?.let { isMember = buffer.readBoolean() }
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeInt(symbol, base, baseColor, simple, simpleColor)
        isMember?.let { buffer.writeBoolean(it) }
    }
}