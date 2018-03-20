package net.ayataka.marinetooler.pigg.network.packet.data.shop

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class ShopSetItemData : PacketData {
    var itemCode = ""
    var itemCategory = ""
    var itemName = ""
    var itemQuantity = 0
    var itemType = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.readFrom(buffer)
    }

    fun readFrom(buffer: ByteBuilder, hasType: Boolean = false) {
        itemCode = buffer.readString()
        itemCategory = buffer.readString()
        itemName = buffer.readString()
        itemQuantity = buffer.readInt()

        if (hasType) {
            itemType = buffer.readString()
        }
    }

    override fun writeTo(buffer: ByteBuilder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}