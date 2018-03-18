package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefinePet
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceActionItem
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlacePet
import net.ayataka.marinetooler.pigg.network.packet.data.player.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.readUCodesFromAreaPacket
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.toHexString

open class BaseAreaData : Packet() {
    override val server = ServerType.CHAT
    override val packetId = InfoPacketID.NONE.id

    var users = mutableListOf<String>()

    lateinit var areaData: AreaData

    var isAdmin = false
    var isPatrol = false
    var isChannelActor = false
    var serverTime: Double = 0.0
    var isRefleshedCosmeItem = false
    var isAllowRoomChange = false

    var loc9 = 0

    var placeFurnitures = mutableListOf<PlaceFurniture>()
    var defineFurnitures = mutableListOf<DefineFurniture>()
    var placeAvatars = mutableListOf<PlaceAvatar>()
    var defineAvatars = mutableListOf<DefineAvatar>()
    var placePets = mutableListOf<PlacePet>()
    var definePets = mutableListOf<DefinePet>()
    var placeActionItems = mutableListOf<PlaceActionItem>()
    var loc11 = mutableMapOf<String, String>()

    //一応動作は確認してるけどもっとデバッグが必要
    override fun readFrom(buffer: ByteBuilder) {
        dump("[Def] ${buffer.array().toHexString()}")
        this.areaData = AreaData()

        this.areaData.readFrom(buffer)

        // Get area users
        this.users = readUCodesFromAreaPacket(buffer)

        for(i in 1..buffer.readInt()){
            val placeFurniture = PlaceFurniture()

            placeFurniture.characterId = buffer.readString()
            placeFurniture.sequence = buffer.readInt()

            placeFurniture.x = buffer.readShort()
            placeFurniture.y = buffer.readShort()
            placeFurniture.z = buffer.readShort()

            placeFurniture.direction = buffer.readByte()
            placeFurniture.ownerId = buffer.readString()

            dump("[Furniture] ID: ${placeFurniture.characterId}, Sequence: ${placeFurniture.sequence}, X: ${placeFurniture.x}, Y: ${placeFurniture.y}, Z: ${placeFurniture.z}, Direction: ${placeFurniture.direction}, ownerId: ${placeFurniture.ownerId}")
            placeFurnitures.add(placeFurniture)
        }

        for(i in 1..buffer.readInt()){
            val loc13 = buffer.readShort()
            val defineFurniture = DefineFurniture()

            defineFurniture.characterId = buffer.readString()
            defineFurniture.type = buffer.readByte()
            defineFurniture.category = buffer.readString()
            defineFurniture.name = buffer.readString()
            defineFurniture.description = buffer.readString()
            defineFurniture.actionCode = buffer.readString()

            for (i2 in 1..loc13){
                val partData = PartData(true)

                partData.readFrom(buffer)

                partData.index = i2

                defineFurniture.parts.add(partData)
            }

            defineFurnitures.add(defineFurniture)
        }

        val loc3 = buffer.readInt()

        for(i in 1..loc3){
            val placeAvatar = PlaceAvatar()
            val defineAvatar = DefineAvatar()
            val avatarData = AvatarData()

            avatarData.readFrom(buffer)

            defineAvatar.characterId = avatarData.userCode

            if(avatarData.amebaId != "") defineAvatar.name = avatarData.amebaId
            else defineAvatar.name = avatarData.nickName

            defineAvatar.data = avatarData

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

            this.placeAvatars.add(placeAvatar)
            this.defineAvatars.add(defineAvatar)
        }

        for(i in 1..buffer.readInt()){
            val definePet = DefinePet()

            definePet.data.readFrom(buffer)

            this.definePets.add(definePet)

            val placePet = PlacePet()

            placePet.petId = definePet.getPetID()

            placePet.x = buffer.readShort()
            placePet.y = buffer.readShort()
            placePet.z = buffer.readShort()

            placePet.direction = buffer.readByte()
            placePet.sleeping = buffer.readBoolean()

            this.placePets.add(placePet)
        }

        for(i in 1..buffer.readInt()){
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

            this.placeActionItems.add(placeActionItem)
        }

        for(i in 1..buffer.readInt()){
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
            this.placeActionItems.add(placeActionItem)
        }

        this.loc9 = buffer.readInt()

        this.isAdmin = (this.loc9 and 1) > 0
        this.isPatrol = (this.loc9 and 2) > 0
        this.isChannelActor = buffer.readBoolean()
        this.serverTime = buffer.readDouble()
        this.isRefleshedCosmeItem = buffer.readBoolean()
        this.isAllowRoomChange = buffer.readBoolean()

        for(i in 1..buffer.readByte()){
            val loc10 = buffer.readString()

            this.loc11[loc10] = loc10
        }

        this.defineAvatars.filter { this.loc11[it.characterId] != null }.forEach { it.friend = true }
    }

    //TODO: 未完成だから仕上げる
    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        var bb = this.areaData.writeTo(buffer)

        bb.writeRawInt(this.placeFurnitures.size)

        for (placeFurniture in this.placeFurnitures) {
            bb.writeString(placeFurniture.characterId)
            bb.writeRawInt(placeFurniture.sequence)

            bb.writeRawShort(placeFurniture.x)
            bb.writeRawShort(placeFurniture.y)
            bb.writeRawShort(placeFurniture.z)

            bb.writeRawByte(placeFurniture.direction)
            bb.writeString(placeFurniture.ownerId)
        }

        bb.writeRawInt(this.defineFurnitures.size)

        for (defineFurniture in this.defineFurnitures) {
            bb.writeRawShort(defineFurniture.parts.size.toShort())

            bb.writeString(defineFurniture.characterId)

            bb.writeRawByte(defineFurniture.type)

            bb.writeString(defineFurniture.category)
            bb.writeString(defineFurniture.name)
            bb.writeString(defineFurniture.description)
            bb.writeString(defineFurniture.actionCode)

            for (part in defineFurniture.parts) {
                bb = part.writeTo(bb)
            }
        }

        bb.writeRawInt(this.placeAvatars.size)

        for(i in 1..this.placeAvatars.size){
            val placeAvatar = this.placeAvatars[i - 1]
            val defineAvatar = this.defineAvatars[i - 1]
            val avatarData = defineAvatar.data

            bb = avatarData.writeTo(bb)

            bb.writeRawShort(placeAvatar.x)
            bb.writeRawShort(placeAvatar.y)
            bb.writeRawShort(placeAvatar.z)

            bb.writeRawByte(placeAvatar.direction)
            bb.writeRawByte(placeAvatar.status)
            bb.writeRawByte(placeAvatar.tired)
            bb.writeRawByte(placeAvatar.mode)
        }

        bb.writeRawInt(this.definePets.size)

        for (i in 1..this.definePets.size ) {
            val definePet = this.definePets[i - 1]
            val placePet = this.placePets[i - 1]

            bb = definePet.data.writeTo(bb)

            bb.writeRawShort(placePet.x)
            bb.writeRawShort(placePet.y)
            bb.writeRawShort(placePet.z)

            bb.writeRawByte(placePet.direction)
            bb.writeBoolean(placePet.sleeping)

        }

        bb.writeRawInt(this.placeActionItems.size)

        for (placeActionItem in this.placeActionItems) {
            if(placeActionItem.mode == 0){
                bb.writeString(placeActionItem.itemType)
                bb.writeString(placeActionItem.itemCode)
                bb.writeString(placeActionItem.ownerCode)
                bb.writeRawInt(placeActionItem.sequence)

                bb.writeRawByte(placeActionItem.actionItemType)

                bb.writeRawShort(placeActionItem.x)
                bb.writeRawShort(placeActionItem.y)
                bb.writeRawShort(placeActionItem.z)
            }
            else{
                bb.writeString(placeActionItem.itemCode)
                bb.writeString(placeActionItem.itemType)
                bb.writeRawInt(placeActionItem.sequence)
                bb.writeString(placeActionItem.ownerCode)

                bb.writeRawShort(placeActionItem.x)
                bb.writeRawShort(placeActionItem.y)
                bb.writeRawShort(placeActionItem.z)
            }
        }

        bb.writeRawInt(this.loc9)

        bb.writeBoolean(this.isChannelActor)
        bb.writeRawDouble(this.serverTime)
        bb.writeBoolean(this.isRefleshedCosmeItem)
        bb.writeBoolean(this.isAllowRoomChange)

        bb.writeRawByte(this.loc11.size.toByte())

        for (mutableEntry in this.loc11) {
            bb.writeString(mutableEntry.key)
        }

        dump("[Write] ${bb.build().array().toHexString()}")

        return null
    }
}