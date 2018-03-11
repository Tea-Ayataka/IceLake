package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

open class ShopSetItemData {
    var itemCode = ""
    var itemCategory = ""
    var itemName = ""
    var itemQuantity = 0
    var itemType = ""

    fun readFrom(buffer: ByteBuilder, hasType: Boolean = false) {
        this.itemCode = buffer.readString()
        this.itemCategory = buffer.readString()
        this.itemName = buffer.readString()
        this.itemQuantity = buffer.readInt()

        if (hasType) {
            this.itemType = buffer.readString()
        }
    }
}