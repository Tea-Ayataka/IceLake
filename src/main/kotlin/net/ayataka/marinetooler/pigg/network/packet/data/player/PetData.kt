package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class PetData {
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

    fun isSelf(): Boolean{
        return owner == CurrentUser.usercode
    }

    fun readFrom(buffer: ByteBuilder)
    {
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

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeRawInt(petId)
                .writeString(type)
                .writeString(name)
                .writeString(owner)
                .writeRawInt(treasuresID)
                .writeString(treasuresCode)
                .writeRawByte(colorId)
                .writeRawByte(gender)
                .writeRawByte(levelFeel)
                .writeRawByte(levelFriendly)
                .writeString(category)
    }
}