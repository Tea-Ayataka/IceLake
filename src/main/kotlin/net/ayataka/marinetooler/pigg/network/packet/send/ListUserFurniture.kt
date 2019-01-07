package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ListUserFurniture : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_USER_FURNITURE.id

    override fun readFrom(buffer: ByteBuilder) {

    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return buffer
    }
}