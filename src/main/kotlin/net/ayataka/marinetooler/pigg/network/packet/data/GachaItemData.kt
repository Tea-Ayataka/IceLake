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
        code = buffer.readString()
        type = buffer.readString()
        name = buffer.readString()
        rarity = buffer.readByte()
        quantity = buffer.readInt()
        setItemSize = buffer.readInt()

        (0 until setItemSize).forEach {
            val data = ShopSetItemData()
            data.readFrom(buffer)
            setItemData.add(data)
        }
    }
}