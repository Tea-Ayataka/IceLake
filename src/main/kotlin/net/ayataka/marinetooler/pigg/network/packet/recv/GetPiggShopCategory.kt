package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetPiggShopCategory : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_PIGGSHOP_CATEGORY.id

    var type = ""
    var category = ""

    override fun readFrom(buffer: ByteBuilder) {
        type = buffer.readString()
        category = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(type)
        buffer.writeString(category)
        return buffer
    }
}