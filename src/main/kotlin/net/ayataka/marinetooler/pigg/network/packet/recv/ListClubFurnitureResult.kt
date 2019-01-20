package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.area.StockFurniture

class ListClubFurnitureResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_CLUB_FURNITURE_RESULT.id

    var isEnableInvite = false
    var isEnabledRoomChange = false
    var existsContribution = false

    var clubContributionInsentive = 0
    var sizeMax = 0
    var capacityNum = 0
    var totalNum = 0
    var categoryTotalNum = 0
    var offset = 0
    var furnitures = mutableListOf<StockFurniture>()
    var data = ByteArray(0)

    override fun readFrom(buffer: ByteBuilder) {
        isEnableInvite = buffer.readBoolean()
        isEnabledRoomChange = buffer.readBoolean()
        existsContribution = buffer.readBoolean()

        if(!isEnabledRoomChange){
            return
        }

        clubContributionInsentive = buffer.readInt()
        sizeMax = buffer.readInt()
        capacityNum = buffer.readInt()
        totalNum = buffer.readInt()
        categoryTotalNum = buffer.readInt()
        offset = buffer.readInt()

        (0 until buffer.readInt()).forEach {
            furnitures.add(StockFurniture().apply {
                quantity = buffer.readInt()
                characterId = buffer.readString()
                type = buffer.readByte()
                category = buffer.readString()
                name = buffer.readString()
                description = buffer.readString()
                actionCode = buffer.readString()

                (0 until buffer.readShort()).forEach {
                    parts.add(PartData(false).apply { readFrom(buffer) })
                }

                time = buffer.readDouble()
            })
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeBoolean(isEnableInvite, isEnabledRoomChange, existsContribution)

        if(!isEnabledRoomChange){
            return buffer
        }

        buffer.writeInt(clubContributionInsentive, sizeMax, capacityNum, totalNum, categoryTotalNum, offset, furnitures.size)

        furnitures.forEach {
            buffer.writeInt(it.quantity)
                    .writeString(it.characterId)
                    .writeByte(it.type)
                    .writeString(it.category, it.name, it.description, it.actionCode)
                    .writeShort(it.parts.size.toShort())

            it.parts.forEach {
                it.writeTo(buffer)
            }

            buffer.writeDouble(it.time)
        }

        return buffer
    }
}