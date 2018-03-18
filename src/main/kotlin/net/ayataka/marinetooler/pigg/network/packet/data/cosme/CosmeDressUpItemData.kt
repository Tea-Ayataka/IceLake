package net.ayataka.marinetooler.pigg.network.packet.data.cosme

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class CosmeDressUpItemData {
    var type = ""
    var itemCode = ""
    var newParts = false

    fun readFrom(buffer: ByteBuilder) {
        itemCode = buffer.readString()
        type = buffer.readString()
        newParts = buffer.readBoolean()
    }

    fun writeTo(): ByteBuilder {
        return ByteBuilder()
                .writeString(itemCode)
                .writeString(type)
                .writeBoolean(newParts)
    }
}