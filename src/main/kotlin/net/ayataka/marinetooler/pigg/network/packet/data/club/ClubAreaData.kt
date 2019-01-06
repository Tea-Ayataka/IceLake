package net.ayataka.marinetooler.pigg.network.packet.data.club

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class ClubAreaData : PacketData {
    var categoryCode = ""
    var areaCode = ""
    var name = ""
    var description = ""
    var memberCount = 0
    var isMaster = false
    var isSubMaster = false
    var number = 0
    var time = 0.0
    var clubEmblem = ClubEmblemData()
    var capacity = 0
    var currentCount = 0
    var updateTime = 0
    var isMessageboard = false
    var contributionMinutesAgo = 0
    var isApply = false
    var isComeNewMember = false
    var isNewClub = false

    override fun readFrom(buffer: ByteBuilder) {
        categoryCode = buffer.readString()
        areaCode = buffer.readString()
        name = buffer.readString()
        description = buffer.readString()
        memberCount = buffer.readInt()
        isMaster = buffer.readBoolean()
        isSubMaster = buffer.readBoolean()
        number = buffer.readInt()
        time = buffer.readDouble()

        clubEmblem.readFrom(buffer)

        capacity = buffer.readInt()
        currentCount = buffer.readInt()
        updateTime = buffer.readInt()
        isMessageboard = buffer.readBoolean()
        contributionMinutesAgo = buffer.readInt()
        isApply = buffer.readBoolean()
        isComeNewMember = buffer.readBoolean()
        isNewClub = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(categoryCode, areaCode, name, description)
                .writeInt(memberCount)
                .writeBoolean(isMaster, isSubMaster)
                .writeInt(number)
                .writeDouble(time)

        clubEmblem.writeTo(buffer)

        buffer.writeInt(capacity, currentCount, updateTime)
                .writeBoolean(isMessageboard)
                .writeInt(contributionMinutesAgo)
                .writeBoolean(isApply, isComeNewMember, isNewClub)
    }
}