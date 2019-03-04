package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.shop.ShopEcItemData
import net.ayataka.marinetooler.utils.trace

class ClickPiggShopItemResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.CLICK_PIGG_SHOP_ITEM_RESULT.id

    var items = listOf<ShopEcItemData>()
    var seriesCode: String = ""
    var seriesTypeCode: String = ""

    override fun readFrom(buffer: ByteBuilder) {
        items = (0 until buffer.readInt()).map {
            ShopEcItemData().apply {
                readData(buffer, true)
                readEcData(buffer)
                shopCode = buffer.readString()
                shopKind = buffer.readByte().toInt()
                sellingState = buffer.readByte().toInt()
                shoppingRelayTargetCode = buffer.readString()
            }
        }

        seriesCode = buffer.readString()
        seriesTypeCode = buffer.readString()

        trace(toString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }

    override fun toString(): String {
        return "ClickPiggShopItemResultPacket(items=${items.joinToString()}, seriesCode='$seriesCode', seriesTypeCode='$seriesTypeCode')"
    }
}