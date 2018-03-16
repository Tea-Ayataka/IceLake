package net.ayataka.marinetooler.pigg.network.packet.data.cosme

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class CosmeDressUpItemData {
    var type = ""
    var itemCode = ""
    var newParts = false

    fun readFrom(buffer: ByteBuilder) {
        this.itemCode = buffer.readString()
        this.type = buffer.readString()
        this.newParts = buffer.readBoolean()
    }

    fun writeTo(): ByteBuilder {
        return ByteBuilder()
                .writeString(this.itemCode)
                .writeString(this.type)
                .writeBoolean(this.newParts)
    }
}