package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

open class ShopSetItemData {
    var itemCode = ""
    var itemCategory = ""
    var itemName = ""
    var itemQuantity = 0
    var itemType = ""

    fun readFrom(buffer: ByteBuilder, hasType: Boolean = false) {
        itemCode = buffer.readString()
        itemCategory = buffer.readString()
        itemName = buffer.readString()
        itemQuantity = buffer.readInt()

        if (hasType) {
            itemType = buffer.readString()
        }
    }
}