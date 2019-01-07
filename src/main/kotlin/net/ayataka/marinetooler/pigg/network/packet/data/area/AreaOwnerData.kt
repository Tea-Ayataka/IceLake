package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class AreaOwnerData : PacketData {
    var amebaId = ""
    var userCode = ""
    var asUserId = ""
    var nickName = ""
    var acceptMessage = false
    var acceptGift = false
    var zone: Byte = 0
    var hasLife = false
    var hasIsland = false
    var hasCafe = false
    var hasWorld = false
    var hasBrave = false
    var hasLeftFootPrintToday = false
    var numFootPrintToday = 0

    override fun readFrom(buffer: ByteBuilder) {
        nickName = buffer.readString()
        amebaId = buffer.readString()
        userCode = buffer.readString()
        asUserId = buffer.readString()
        zone = buffer.readByte()
        hasLeftFootPrintToday = buffer.readBoolean()
        numFootPrintToday = buffer.readInt()
        acceptMessage = buffer.readBoolean()
        acceptGift = buffer.readBoolean()
        hasLife = buffer.readBoolean()
        hasIsland = buffer.readBoolean()
        hasCafe = buffer.readBoolean()
        hasWorld = buffer.readBoolean()
        hasBrave = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(nickName, amebaId, userCode, asUserId)
                .writeByte(zone)
                .writeBoolean(hasLeftFootPrintToday)
                .writeInt(numFootPrintToday)
                .writeBoolean(acceptMessage, acceptGift, hasLife, hasIsland, hasCafe, hasWorld, hasBrave)
    }
}