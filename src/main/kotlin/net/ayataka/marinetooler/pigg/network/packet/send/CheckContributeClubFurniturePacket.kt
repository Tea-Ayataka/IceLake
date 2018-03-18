package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

class CheckContributeClubFurniturePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.CHECK_CONTRIBUTE_CLUB_FURNITURE.id

    var areaCode = ""
    var furnitureId = ""

    override fun readFrom(buffer: ByteBuilder) {
        areaCode = buffer.readString()
        furnitureId = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}