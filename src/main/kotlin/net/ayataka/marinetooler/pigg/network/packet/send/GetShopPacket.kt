package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class GetShopPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_SHOP.id
    override val encrypted = true

    var shopCode = ""
    var isAdminRequest = false

    override fun readFrom(buffer: ByteBuilder) {
        shopCode = buffer.readString()
        isAdminRequest = buffer.readBoolean()

        info("SHOPCODE IS ${shopCode}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(shopCode)
        buffer.writeBoolean(isAdminRequest)
        return buffer
    }
}