package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class CheckContributeClubFurnitureResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.CHECK_CONTRIBUTE_CLUB_FURNITURE_RESULT.id

    var isContributable = true
    var isNonContributableFurniture = false
    var isOverFurnitureTypeMax = false
    var isOverFurnitureMax = false

    override fun readFrom(buffer: ByteBuilder) {
        this.isContributable = buffer.readBoolean()
        this.isNonContributableFurniture = buffer.readBoolean()
        this.isOverFurnitureTypeMax = buffer.readBoolean()
        this.isOverFurnitureMax = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeBoolean(this.isContributable)
        buffer.writeBoolean(this.isNonContributableFurniture)
        buffer.writeBoolean(this.isOverFurnitureTypeMax)
        buffer.writeBoolean(this.isOverFurnitureMax)
        return buffer
    }
}