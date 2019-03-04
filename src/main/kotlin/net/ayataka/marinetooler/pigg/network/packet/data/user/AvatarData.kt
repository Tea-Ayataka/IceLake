package net.ayataka.marinetooler.pigg.network.packet.data.user

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData
import net.ayataka.marinetooler.pigg.network.packet.data.cosme.CosmeDressUpItemData

class AvatarData : PacketData {
    companion object {
        var COSME_CONTACT_IGNORE_INDEXES = arrayOf(71)
        var COSME_EYELASH_IGNORE_INDEXES = arrayOf(67, 70, 71, 77, 80, 82)
        var COSME_EYELINE_IGNORE_INDEXES = arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 35, 66, 67, 68, 69, 70, 71, 76, 77, 78, 79, 80, 81, 82, 84, 85)
        var COSME_EYESHADOW_IGNORE_INDEXES = arrayOf(67, 70, 71, 77, 78, 82)
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

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString()
        amebaId = buffer.readString()
        asUserId = buffer.readString()
        nickName = buffer.readString()
        part.gender = buffer.readByte()
        part.readFrom(buffer)
        color.readFrom(buffer)
        position.readFrom(buffer)

        (0 until buffer.readByte()).forEach {
            item.items.add(buffer.readString())
        }

        (0 until buffer.readByte()).forEach {
            val cosmeData = CosmeDressUpItemData()
            cosmeData.readFrom(buffer)
            cosme.add(cosmeData)
        }

        (0 until buffer.readByte()).forEach {
            conditions.add(buffer.readString())
        }
    }

    override fun writeTo(buffer: ByteBuilder) {
        // 基本データ
        buffer.writeString(userCode, amebaId, asUserId, nickName)

        part.writeTo(buffer)
        color.writeTo(buffer)
        position.writeTo(buffer)

        // アイテム
        buffer.writeByte(item.items.size.toByte())
        buffer.writeString(*item.items.toTypedArray())

        // コスメ
        buffer.writeByte(cosme.size.toByte())
        cosme.forEach {
            buffer.writeString(it.itemCode, it.type)
                    .writeBoolean(it.newParts)
        }

        // コンディション(?)
        buffer.writeByte(conditions.size.toByte())
        buffer.writeString(*conditions.toTypedArray())
    }

    override fun toString(): String {
        return "AvatarData(userCode='$userCode', amebaId='$amebaId', nickName='$nickName', asUserId='$asUserId', color=$color, part=$part, position=$position, item=$item, conditions=$conditions, cosme=$cosme)"
    }
}