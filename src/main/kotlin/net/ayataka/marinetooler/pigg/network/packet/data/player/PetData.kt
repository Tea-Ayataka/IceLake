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
        return this.owner == CurrentUser.usercode
    }

    fun readFrom(buffer: ByteBuilder)
    {
        this.petId = buffer.readInt()
        this.type = buffer.readString()
        this.name = buffer.readString()
        this.owner = buffer.readString()
        this.treasuresID = buffer.readInt()
        this.treasuresCode = buffer.readString()
        this.colorId = buffer.readByte()
        this.gender = buffer.readByte()
        this.levelFeel = buffer.readByte()
        this.levelFriendly = buffer.readByte()
        this.category = buffer.readString()
    }

}