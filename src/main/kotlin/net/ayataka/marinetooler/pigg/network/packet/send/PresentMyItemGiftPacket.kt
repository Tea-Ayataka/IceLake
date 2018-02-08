package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.UserItemData

class PresentMyItemGiftPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PRESENT_MY_ITEM_GIFT.id
    override val encrypted = true

    var usercode = ""
    var message = ""
    var items = arrayListOf<UserItemData>()

    override fun readFrom(buffer: ByteBuilder) {
        this.usercode = buffer.readString()
        this.message = buffer.readString()
        val itemsLength = buffer.readInt()

        for (i in 0 until itemsLength) {
            this.items.add(UserItemData(buffer.readString(), buffer.readString(), buffer.readInt(), buffer.readInt()))
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.usercode)
        buffer.writeString(this.message)
        buffer.writeRawInt(this.items.size)

        this.items.forEach {
            buffer.writeString(it.item)
            buffer.writeString(it.type)
            buffer.writeRawInt(it.id)
            buffer.writeRawInt(it.flag)
        }

        buffer.writeDoubleTimeStamp()

        return buffer
    }
}