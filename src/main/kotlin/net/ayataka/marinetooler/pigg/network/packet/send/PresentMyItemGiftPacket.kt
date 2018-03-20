package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.shop.UserItemData

class PresentMyItemGiftPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PRESENT_MY_ITEM_GIFT.id
    override val encrypted = true

    var usercode = ""
    var message = ""
    var items = arrayListOf<UserItemData>()

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString()
        message = buffer.readString()
        val itemsLength = buffer.readInt()

        for (i in 0 until itemsLength) {
            items.add(UserItemData(buffer.readString(), buffer.readString(), buffer.readInt(), buffer.readInt()))
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(usercode)
        buffer.writeString(message)
        buffer.writeInt(items.size)

        items.forEach {
            buffer.writeString(it.item)
            buffer.writeString(it.type)
            buffer.writeInt(it.id)
            buffer.writeInt(it.flag)
        }

        buffer.writeDoubleTimeStamp()

        return buffer
    }
}