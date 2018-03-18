package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.ShopGachaData

class GetPiggShopGachaResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_PIGGSHOP_GACHA_RESULT.id

    var type = ""
    var category = ""
    var items = mutableListOf<ShopGachaData>()

    override fun readFrom(buffer: ByteBuilder) {
        type = buffer.readString()
        category = buffer.readString()

        val length = buffer.readInt()

        (0 until length).forEach {
            val data = ShopGachaData()
            data.readFrom(buffer)
            items.add(data)
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}