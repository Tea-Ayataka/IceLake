package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.cosme.CosmeDressUpItemData

class AvatarData {
    companion object {
        var COSME_CONTACT_IGNORE_INDEXES = arrayOf(71)
        var COSME_EYELASH_IGNORE_INDEXES = arrayOf(67, 70, 71, 77, 80, 82)
        var COSME_EYELINE_IGNORE_INDEXES = arrayOf(0,1,2,3,4,5,6,7,8,9,10,11,35,66,67,68,69,70,71,76,77,78,79,80,81,82,84,85)
        var COSME_EYESHADOW_IGNORE_INDEXES = arrayOf(67,70,71,77,78,82)
    }

    var userCode = ""
    var amebaId = ""
    var nickName = ""
    var asUserId = ""
    var color: BodyColorData = BodyColorData()
    var part: BodyPartData = BodyPartData()
    var position: BodyPositionData = BodyPositionData()
    var item: BodyItemData = BodyItemData()

    var conditions = mutableListOf<String>()
    var cosme = mutableListOf<CosmeDressUpItemData>()

    fun readFrom(buffer: ByteBuilder){
        this.userCode = buffer.readString()
        this.amebaId = buffer.readString()
        this.asUserId = buffer.readString()
        this.nickName = buffer.readString()
        this.part.gender = buffer.readByte()
        this.part.readFrom(buffer)
        this.color.readFrom(buffer)
        this.position.readFrom(buffer)

        for(i in 1..buffer.readByte()){
            this.item.items.add(buffer.readString())
        }

        for(i in 1..buffer.readByte()){
            val data = CosmeDressUpItemData()

            data.readFrom(buffer)

            this.cosme.add(data)
        }

        for(i in 1..buffer.readByte()){
            this.conditions.add(buffer.readString())
        }
    }

    fun writeTo(): ByteBuilder{
        var bb = ByteBuilder()

        bb.writeString(this.userCode)
        .writeString(this.amebaId)
        .writeString(this.asUserId)
        .writeString(this.nickName)

        bb = this.part.writeTo(bb)
        bb = this.color.writeTo(bb)
        bb = this.position.writeTo(bb)

        bb.writeRawByte(this.item.items.size.toByte())

        this.item.items.forEach { bb.writeString(it) }

        bb.writeRawByte(this.cosme.size.toByte())

        this.cosme.forEach {
            bb.writeString(it.itemCode)
                    .writeString(it.type)
                    .writeBoolean(it.newParts)
        }

        bb.writeRawByte(this.conditions.size.toByte())

        this.conditions.forEach { bb.writeString(it) }

        return bb
    }
}