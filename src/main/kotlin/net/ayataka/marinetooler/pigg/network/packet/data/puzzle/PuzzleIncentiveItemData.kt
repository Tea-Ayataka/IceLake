package net.ayataka.marinetooler.pigg.network.packet.data.puzzle

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class PuzzleIncentiveItemData : PacketData {
    var itemCode = ""
    var itemType = ""
    var itemName = ""
    var itemQuantity = 0

    override fun readFrom(buffer: ByteBuilder) {
        itemCode = buffer.readString()
        itemType = buffer.readString()
        itemName = buffer.readString()
        itemQuantity = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder) {

    }
}