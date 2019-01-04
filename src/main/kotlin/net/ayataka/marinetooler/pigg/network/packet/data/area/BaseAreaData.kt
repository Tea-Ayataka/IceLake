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
    var data = ByteArray(0)

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

            //dump("[Furniture] ID: ${furniture.characterId}, Sequence: ${furniture.sequence}, X: ${furniture.x}, Y: ${furniture.y}, Z: ${furniture.z}, Direction: ${furniture.direction}, ownerId: ${furniture.ownerId}")
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

        data = buffer.readAllBytes()
    }

    override fun writeTo(buffer: ByteBuilder) : ByteBuilder? {
        areaData.writeTo(buffer)

        buffer.writeInt(placeFurnitures.size)

        placeFurnitures.forEach {
            buffer.writeString(it.characterId)
            buffer.writeInt(it.sequence)

            buffer.writeShort(it.x, it.y, it.z)

            buffer.writeByte(it.direction)
            buffer.writeString(it.ownerId)
        }

        buffer.writeInt(defineFurnitures.size)

        defineFurnitures.forEach {
            buffer.writeShort(it.parts.size.toShort())

            buffer.writeString(it.characterId)

            buffer.writeByte(it.type)

            buffer.writeString(it.category)
            buffer.writeString(it.name)
            buffer.writeString(it.description)
            buffer.writeString(it.actionCode)

            it.parts.forEach {
                it.writeTo(buffer)
            }
        }

        buffer.writeInt(placeAvatars.size)

        placeAvatars.forEachIndexed { index, placeAvatar ->
            val defineAvatar = defineAvatars[index]
            val avatarData = defineAvatar.data

            avatarData.writeTo(buffer)

            buffer.writeShort(placeAvatar.x, placeAvatar.y, placeAvatar.z)

            buffer.writeByte(placeAvatar.direction, placeAvatar.status, placeAvatar.tired, placeAvatar.mode)
        }

        buffer.writeInt(definePets.size)

        placePets.forEachIndexed { index, placePet ->
            val definePet = definePets[index]

            definePet.data.writeTo(buffer)

            buffer.writeShort(placePet.x, placePet.y, placePet.z)

            buffer.writeByte(placePet.direction)
            buffer.writeBoolean(placePet.sleeping)
        }

        buffer.writeInt(placeActionItems.count { it.mode == 0 })

        placeActionItems.filter { it.mode == 0 }.forEach {
            buffer.writeString(it.itemType, it.itemCode, it.ownerCode)
            buffer.writeInt(it.sequence)

            buffer.writeByte(it.actionItemType)

            buffer.writeShort(it.x, it.y, it.z)
        }

        buffer.writeInt(placeActionItems.count { it.mode == 1 })

        placeActionItems.filter { it.mode == 1 }.forEach {
            buffer.writeString(it.itemCode, it.itemType)
            buffer.writeInt(it.sequence)
            buffer.writeString(it.ownerCode)

            buffer.writeShort(it.x, it.y, it.z)
        }

        buffer.writeInt(meta)

        buffer.writeBoolean(isChannelActor)
        buffer.writeDouble(serverTime)
        buffer.writeBoolean(isRefreshedCosmeItem, isAllowRoomChange)

        buffer.writeByte(loc11.size.toByte())

        buffer.writeString(*loc11.values.toTypedArray())

        buffer.writeRawBytes(data)

        return buffer
    }

    override fun toString(): String {
        return "BaseAreaData(areaData=$areaData, defineAvatars=$defineAvatars)"
    }
}