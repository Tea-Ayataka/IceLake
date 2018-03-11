package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

class ContributeClubFurniturePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.CONTRIBUTE_CLUB_FURNITURE.id

    var areaCode = ""
    var furnitureId = ""
    var msg = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.areaCode = buffer.readString()
        this.furnitureId = buffer.readString()
        this.msg = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.areaCode)
        buffer.writeString(this.furnitureId)
        buffer.writeString(this.msg)

        return buffer
    }
}