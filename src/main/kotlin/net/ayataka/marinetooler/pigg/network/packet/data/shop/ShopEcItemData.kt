package net.ayataka.marinetooler.pigg.network.packet.data.shop

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class ShopEcItemData : ShopItemData() {
    var subcategory: String = ""
    var colorCodes: List<Int> = listOf()
    var favorite: Boolean = false
    var limitedRemainTime: Double = 0.0
    var shopCode: String = ""
    var shopKind: Int = 0
    var sellingState: Int = 0
    var showDetailFrom: String = ""
    var shoppingRelayTargetCode: String = ""
    var nextShoppingRelayStep: Int = 0
    var bulkFittingMode: Int = 1

    fun readEcData(buffer: ByteBuilder) {
        subcategory = buffer.readString()
        colorCodes = (0 until buffer.readInt()).map { buffer.readByte().toInt() }
        favorite = buffer.readBoolean()
        limitedRemainTime = buffer.readDouble()
    }

    override fun toString(): String {
        return "ShopEcItemData(subcategory='$subcategory', colorCodes=$colorCodes, favorite=$favorite, limitedRemainTime=$limitedRemainTime, shopCode='$shopCode', shopKind=$shopKind, sellingState=$sellingState, showDetailFrom='$showDetailFrom', shoppingRelayTargetCode='$shoppingRelayTargetCode', nextShoppingRelayStep=$nextShoppingRelayStep, bulkFittingMode=$bulkFittingMode) ${super.toString()}"
    }
}