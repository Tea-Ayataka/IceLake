package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ListUserItemResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_USER_ITEM_RESULT.id

    override fun readFrom(buffer: ByteBuilder) {
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}