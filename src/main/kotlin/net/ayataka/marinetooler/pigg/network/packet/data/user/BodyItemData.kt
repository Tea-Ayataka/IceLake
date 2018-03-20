package net.ayataka.marinetooler.pigg.network.packet.data.user

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class BodyItemData : PacketData {
    var items = mutableListOf<String>()

    override fun readFrom(buffer: ByteBuilder) {
        (0 until buffer.readInt()).forEach {
            items.add(buffer.readString())
        }
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeInt(items.size)
        buffer.writeString(*items.toTypedArray())
    }
}