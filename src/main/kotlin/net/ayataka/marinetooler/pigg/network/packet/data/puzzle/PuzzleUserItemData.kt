package net.ayataka.marinetooler.pigg.network.packet.data.puzzle

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class PuzzleUserItemData {
    var itemCode = ""
    var itemCategory = ""
    var itemName = ""
    var itemDescription = ""
    var itemExpireTime = 0.0
    var itemQuantity = 0
    var itemMagnification = 0

    fun readFrom(buffer: ByteBuilder, magnification: Boolean = true) {
        itemCode = buffer.readString()
        itemCategory = buffer.readString()
        itemName = buffer.readString()
        itemDescription = buffer.readString()
        itemExpireTime = buffer.readDouble()
        itemQuantity = buffer.readInt()

        if (magnification) {
            itemMagnification = buffer.readInt()
        }
    }

    fun writeTo(buffer: ByteBuilder, magnification: Boolean = true) {
        buffer.writeString(itemCode)
        buffer.writeString(itemCategory)
        buffer.writeString(itemName)
        buffer.writeString(itemDescription)
        buffer.writeDouble(itemExpireTime)
        buffer.writeInt(itemQuantity)

        if(magnification) {
            buffer.writeInt(itemMagnification)
        }
    }
}