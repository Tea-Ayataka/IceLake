package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class BodyItemData {
    var items = mutableListOf<String>()
    var size = 0

    fun readFrom(buffer: ByteBuilder){
        size = buffer.readInt()
        for (i in 1..size){
            items.add(buffer.readString())
        }
    }

    fun writeTo(): ByteBuilder{
        val bb = ByteBuilder()

        bb.writeRawInt(size)

        items.forEach { bb.writeString(it) }

        return bb
    }
}