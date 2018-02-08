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
        this.shopCode = buffer.readString()
        this.isAdminRequest = buffer.readBoolean()

        info("SHOPCODE IS ${this.shopCode}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.shopCode)
        buffer.writeBoolean(this.isAdminRequest)
        return buffer
    }
}