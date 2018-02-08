package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ClickPiggShopItemPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.CLICK_PIGG_SHOP_ITEM.id
    override val encrypted = true

    var shopCode = ""
    var itemType = ""
    var itemCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.shopCode = buffer.readString()
        this.itemType = buffer.readString()
        this.itemCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}