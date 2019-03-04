package net.ayataka.marinetooler.pigg.network.packet.data.user

import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class PetData : PacketData {
    var petId = 0
    var colorId: Byte = 0
    var type = ""
    var name = ""
    var gender: Byte = 0
    var owner = ""
    var character = ""
    var description = ""
    var category = ""
    var treasuresID = 0
    var treasuresCode = ""
    var behaviorType1 = 0
    var behaviorType2 = 0
    var levelFriendly: Byte = 0
    var levelFeel: Byte = 0
    var distressType = ""
    var isTaken = false
    var pointFriendly = 0
    var reachedDaylyMaxPoint = false
    var actions = null

    fun isSelf(): Boolean {
        return owner == CurrentUser.usercode
    }

    override fun readFrom(buffer: ByteBuilder) {
        petId = buffer.readInt()
        type = buffer.readString()
        name = buffer.readString()
        owner = buffer.readString()
        treasuresID = buffer.readInt()
        treasuresCode = buffer.readString()
        colorId = buffer.readByte()
        gender = buffer.readByte()
        levelFeel = buffer.readByte()
        levelFriendly = buffer.readByte()
        category = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeInt(petId)
                .writeString(type, name, owner)
                .writeInt(treasuresID)
                .writeString(treasuresCode)
                .writeByte(colorId, gender, levelFeel, levelFriendly)
                .writeString(category)
    }
}