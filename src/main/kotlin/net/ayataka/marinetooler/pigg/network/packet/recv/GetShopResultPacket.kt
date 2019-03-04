package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.shop.ShopData
import net.ayataka.marinetooler.pigg.network.packet.data.user.BodyColorData
import net.ayataka.marinetooler.pigg.network.packet.data.user.BodyItemData
import net.ayataka.marinetooler.pigg.network.packet.data.user.BodyPartData
import net.ayataka.marinetooler.pigg.network.packet.data.user.BodyPositionData

class GetShopResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_SHOP_RESULT.id

    var zone: Int = 0
    var coin: Int = 0
    var point: Int = 0
    var coupon: Int = 0
    var shop: ShopData = ShopData()
    var bodyPart: BodyPartData = BodyPartData()
    var bodyColor: BodyColorData = BodyColorData()
    var bodyPosition: BodyPositionData = BodyPositionData()
    var bodyItem: BodyItemData = BodyItemData()
    var bodyCosme: Array<Int> = arrayOf()

    override fun readFrom(buffer: ByteBuilder) {
        shop = ShopData()
        shop.shopType = buffer.readInt()
        zone = buffer.readByte().toInt()

        if (zone == 0) {
            zone = 1
        }

        shop.zone = zone
        shop.zone = zone
        point = buffer.readInt()
        coin = buffer.readInt()
        coupon = buffer.readInt()
        shop.shopCode = buffer.readString()
        shop.name = buffer.readString()
        shop.staffDescription = buffer.readString()
        shop.staffDescription2 = buffer.readString()
        shop.shopTemplateCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}