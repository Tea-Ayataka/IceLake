package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

open class GachaItemData {
    var code = ""
    var type = ""
    var name = ""

    var rarity: Byte = 0
    var quantity = 0

    var setItemSize = 0
    var setItemData = mutableListOf<ShopSetItemData>()

    fun readFrom(buffer: ByteBuilder) {
        this.code = buffer.readString()
        this.type = buffer.readString()
        this.name = buffer.readString()
        this.rarity = buffer.readByte()
        this.quantity = buffer.readInt()
        this.setItemSize = buffer.readInt()

        (0 until this.setItemSize).forEach {
            val data = ShopSetItemData()
            data.readFrom(buffer)
            this.setItemData.add(data)
        }
    }
}