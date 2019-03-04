package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class BuyGiftItem : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.BUY_GIFT_ITEM.id
    override val encrypted = true

    var userCode = ""
    var giftShopCode = ""
    var giftItemCode = ""
    var giftItemType = ""
    var quantity = 0
    var wrappingShopCode = ""
    var wrappingItemCode = ""
    var wrappingItemType = ""
    var giftMessage = ""
    var giftReserveDate = 0.0
    var spendType: Byte = 0
    var categoryType = 0

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString()
        giftShopCode = buffer.readString()
        giftItemCode = buffer.readString()
        giftItemType = buffer.readString()
        quantity = buffer.readInt()
        wrappingShopCode = buffer.readString()
        wrappingItemCode = buffer.readString()
        wrappingItemType = buffer.readString()
        giftMessage = buffer.readString()
        giftReserveDate = buffer.readDouble()
        spendType = buffer.readByte()
        categoryType = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(userCode, giftShopCode, giftItemCode, giftItemType)
                .writeInt(quantity)
                .writeString(wrappingShopCode, wrappingItemCode, wrappingItemType, giftMessage)
                .writeDouble(giftReserveDate)
                .writeByte(spendType)
                .writeInt(categoryType)

        return buffer
    }
}