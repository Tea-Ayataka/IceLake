package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.shop.ShopEcItemData

class GetPiggShopCategoryResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_PIGGSHOP_CATEGORY_RESULT.id

    var type: String = ""
    var category: String = ""
    var items = listOf<ShopEcItemData>()

    override fun readFrom(buffer: ByteBuilder) {
        type = buffer.readString()
        category = buffer.readString()

        val isAme = type == "ame" || type == "gift_ame_item"
        val isGift = type == "gift"

        items = (0 until buffer.readInt()).map {
            ShopEcItemData().apply {
                isAmeShop = isAme
                isGiftOnly = isGift
                readData(buffer, true)
                readEcData(buffer)
                shoppingRelayTargetCode = buffer.readString()
                shopCode = if (isAme) "pigg_ame" else "pigg"
            }
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }

    override fun toString(): String {
        return "GetPiggShopCategoryResult(type='$type', category='$category', items=$items)"
    }
}