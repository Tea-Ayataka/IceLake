package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class PetSolveFurniturePlaceData : PacketData {
    var furnitureId = ""
    var roomIndex = 0
    var countSelf = 0
    var countOther = 0
    var countMax = 0

    override fun readFrom(buffer: ByteBuilder) {
        furnitureId = buffer.readString()
        roomIndex = buffer.readInt()
        countSelf = buffer.readInt()
        countOther = buffer.readInt()
        countMax = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(furnitureId)
                .writeInt(roomIndex, countSelf, countOther, countMax)
    }
}