package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class BodyItemData {
    var items = mutableListOf<String>()
    var size = 0

    fun readFrom(buffer: ByteBuilder){
        this.size = buffer.readInt()
        for (i in 1..this.size){
            this.items.add(buffer.readString())
        }
    }

    fun writeTo(): ByteBuilder{
        val bb = ByteBuilder()

        bb.writeRawInt(this.size)

        this.items.forEach { bb.writeRawString(it) }

        return bb
    }
}