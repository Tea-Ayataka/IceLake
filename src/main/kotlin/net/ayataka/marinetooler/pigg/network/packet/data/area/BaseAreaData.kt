package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefinePet
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceActionItem
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlacePet
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.toHexString

open class BaseAreaData : Packet() {
    override val server = ServerType.CHAT
    override val packetId = InfoPacketID.NONE.id

    var areaData: AreaData = AreaData()

    var isAdmin = false
    var isPatrol = false
    var isChannelActor = false
    var serverTime: Double = 0.0
    var isRefreshedCosmeItem = false
    var isAllowRoomChange = false

    var meta = 0

    var placeFurnitures = mutableListOf<PlaceFurniture>()
    var defineFurnitures = mutableListOf<DefineFurniture>()
    var placeAvatars = mutableListOf<PlaceAvatar>()
    var defineAvatars = mutableListOf<DefineAvatar>()
    var placePets = mutableListOf<PlacePet>()
    var definePets = mutableListOf<DefinePet>()
    var placeActionItems = mutableListOf<PlaceActionItem>()
    var loc11 = mutableMapOf<String, String>()

    override fun readFrom(buffer: ByteBuilder) {
        // エリアデータ
        areaData = AreaData()
        areaData.readFrom(buffer)

        // 家具をロード
        (0 until buffer.readInt()).forEach {
            val furniture = PlaceFurniture()

            furniture.characterId = buffer.readString()
            furniture.sequence = buffer.readInt()

            furniture.x = buffer.readShort()
            furniture.y = buffer.readShort()
            furniture.z = buffer.readShort()

            furniture.direction = buffer.readByte()
            furniture.ownerId = buffer.readString()

            dump("[Furniture] ID: ${furniture.characterId}, Sequence: ${furniture.sequence}, X: ${furniture.x}, Y: ${furniture.y}, Z: ${furniture.z}, Direction: ${furniture.direction}, ownerId: ${furniture.ownerId}")
            placeFurnitures.add(furniture)
        }

        // 特殊家具をロード
        (0 until buffer.readInt()).forEach {
            val partLength = buffer.readShort()

            val furniture = DefineFurniture()
            furniture.characterId = buffer.readString()
            furniture.type = buffer.readByte()
            furniture.category = buffer.readString()
            furniture.name = buffer.readString()
            furniture.description = buffer.readString()
            furniture.actionCode = buffer.readString()

            for (i in 0 until partLength) {
                val partData = PartData(true)
                partData.readFrom(buffer)

                partData.index = i

                furniture.parts.add(partData)
            }

            defineFurnitures.add(furniture)
        }

        // ユーザーを読み込む
        (0 until buffer.readInt()).forEach {
            val avatarData = AvatarData()
            avatarData.readFrom(buffer)

            val defineAvatar = DefineAvatar()
            defineAvatar.load(avatarData)

            val placeAvatar = PlaceAvatar()
            placeAvatar.characterId = defineAvatar.characterId

            placeAvatar.x = buffer.readShort()
            placeAvatar.y = buffer.readShort()
            placeAvatar.z = buffer.readShort()

            placeAvatar.direction = buffer.readByte()
            placeAvatar.status = buffer.readByte()
            placeAvatar.tired = buffer.readByte()
            placeAvatar.mode = buffer.readByte()

            val partData = PartData(false)
            partData.height = 96

            defineAvatar.part = partData

            placeAvatars.add(placeAvatar)
            defineAvatars.add(defineAvatar)
        }

        (0 until buffer.readInt()).forEach {
            val definePet = DefinePet()
            definePet.data.readFrom(buffer)

            definePets.add(definePet)

            val placePet = PlacePet()
            placePet.petId = definePet.data.petId

            placePet.x = buffer.readShort()
            placePet.y = buffer.readShort()
            placePet.z = buffer.readShort()

            placePet.direction = buffer.readByte()
            placePet.sleeping = buffer.readBoolean()

            placePets.add(placePet)
        }

        (0 until buffer.readInt()).forEach {
            val placeActionItem = PlaceActionItem()

            placeActionItem.itemType = buffer.readString()
            placeActionItem.itemCode = buffer.readString()
            placeActionItem.ownerCode = buffer.readString()
            placeActionItem.sequence = buffer.readInt()

            placeActionItem.actionItemType = buffer.readByte()

            placeActionItem.x = buffer.readShort()
            placeActionItem.y = buffer.readShort()
            placeActionItem.z = buffer.readShort()

            placeActionItem.mode = 0

            placeActionItems.add(placeActionItem)
        }

        // アクション
        (0 until buffer.readInt()).forEach {
            val placeActionItem = PlaceActionItem()

            placeActionItem.itemCode = buffer.readString()
            placeActionItem.itemType = buffer.readString()
            placeActionItem.sequence = buffer.readInt()
            placeActionItem.ownerCode = buffer.readString()

            placeActionItem.x = buffer.readShort()
            placeActionItem.y = buffer.readShort()
            placeActionItem.z = buffer.readShort()

            placeActionItem.actionItemType = 2

            placeActionItem.mode = 1
            placeActionItems.add(placeActionItem)
        }

        meta = buffer.readInt()

        isAdmin = (meta and 1) > 0
        isPatrol = (meta and 2) > 0

        isChannelActor = buffer.readBoolean()
        serverTime = buffer.readDouble()
        isRefreshedCosmeItem = buffer.readBoolean()
        isAllowRoomChange = buffer.readBoolean()

        // ??
        for (i in 1..buffer.readByte()) {
            val loc10 = buffer.readString()
            loc11[loc10] = loc10
        }

        defineAvatars.filter { loc11[it.characterId] != null }.forEach { it.friend = true }
    }

    //TODO: 未完成だから仕上げる
    override fun writeTo(buffer: ByteBuilder) : ByteBuilder? {
        areaData.writeTo(buffer)

        buffer.writeInt(placeFurnitures.size)

        for (placeFurniture in placeFurnitures) {
            buffer.writeString(placeFurniture.characterId)
            buffer.writeInt(placeFurniture.sequence)

            buffer.writeShort(placeFurniture.x)
            buffer.writeShort(placeFurniture.y)
            buffer.writeShort(placeFurniture.z)

            buffer.writeByte(placeFurniture.direction)
            buffer.writeString(placeFurniture.ownerId)
        }

        buffer.writeInt(defineFurnitures.size)

        for (defineFurniture in defineFurnitures) {
            buffer.writeShort(defineFurniture.parts.size.toShort())

            buffer.writeString(defineFurniture.characterId)

            buffer.writeByte(defineFurniture.type)

            buffer.writeString(defineFurniture.category)
            buffer.writeString(defineFurniture.name)
            buffer.writeString(defineFurniture.description)
            buffer.writeString(defineFurniture.actionCode)

            for (part in defineFurniture.parts) {
                part.writeTo(buffer)
            }
        }

        buffer.writeInt(placeAvatars.size)

        for (i in 1..placeAvatars.size) {
            val placeAvatar = placeAvatars[i - 1]
            val defineAvatar = defineAvatars[i - 1]
            val avatarData = defineAvatar.data

            avatarData.writeTo(buffer)

            buffer.writeShort(placeAvatar.x)
            buffer.writeShort(placeAvatar.y)
            buffer.writeShort(placeAvatar.z)

            buffer.writeByte(placeAvatar.direction)
            buffer.writeByte(placeAvatar.status)
            buffer.writeByte(placeAvatar.tired)
            buffer.writeByte(placeAvatar.mode)
        }

        buffer.writeInt(definePets.size)

        for (i in 1..definePets.size) {
            val definePet = definePets[i - 1]
            val placePet = placePets[i - 1]

            definePet.data.writeTo(buffer)

            buffer.writeShort(placePet.x)
            buffer.writeShort(placePet.y)
            buffer.writeShort(placePet.z)

            buffer.writeByte(placePet.direction)
            buffer.writeBoolean(placePet.sleeping)

        }

        buffer.writeInt(placeActionItems.size)

        for (placeActionItem in placeActionItems) {
            if (placeActionItem.mode == 0) {
                buffer.writeString(placeActionItem.itemType)
                buffer.writeString(placeActionItem.itemCode)
                buffer.writeString(placeActionItem.ownerCode)
                buffer.writeInt(placeActionItem.sequence)

                buffer.writeByte(placeActionItem.actionItemType)

                buffer.writeShort(placeActionItem.x)
                buffer.writeShort(placeActionItem.y)
                buffer.writeShort(placeActionItem.z)
            } else {
                buffer.writeString(placeActionItem.itemCode)
                buffer.writeString(placeActionItem.itemType)
                buffer.writeInt(placeActionItem.sequence)
                buffer.writeString(placeActionItem.ownerCode)

                buffer.writeShort(placeActionItem.x)
                buffer.writeShort(placeActionItem.y)
                buffer.writeShort(placeActionItem.z)
            }
        }

        buffer.writeInt(meta)

        buffer.writeBoolean(isChannelActor)
        buffer.writeDouble(serverTime)
        buffer.writeBoolean(isRefreshedCosmeItem)
        buffer.writeBoolean(isAllowRoomChange)

        buffer.writeByte(loc11.size.toByte())

        for (mutableEntry in loc11) {
            buffer.writeString(mutableEntry.key)
        }

        dump("[Write] ${buffer.build().array().toHexString()}")

        return null
    }
}