package net.ayataka.marinetooler.pigg.network.packet.data.cosme

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class CosmeDressUpItemData : PacketData {
    var type = ""
    var itemCode = ""
    var newParts = false

    override fun readFrom(buffer: ByteBuilder) {
        itemCode = buffer.readString()
        type = buffer.readString()
        newParts = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(itemCode, type)
                .writeBoolean(newParts)
    }
}