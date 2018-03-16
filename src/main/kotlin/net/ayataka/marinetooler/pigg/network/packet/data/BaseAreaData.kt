package net.ayataka.marinetooler.pigg.network.packet.data

import com.flagstone.transform.Place
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.player.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefinePet
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceActionItem
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlacePet
import net.ayataka.marinetooler.pigg.network.packet.readUCodesFromAreaPacket

open class BaseAreaData : Packet() {
    override val server = ServerType.CHAT
    override val packetId = InfoPacketID.NONE.id

    var users = mutableListOf<String>()

    lateinit var areaData: AreaData

    var isAdmin = false
    var isPatrol = false
    var c = false
    var serverTime: Double = 0.0
    var isRefleshedCosmeItem = false
    var isAllowRoomChange = false

    var placeFurnitures = mutableListOf<PlaceFurniture>()
    var defineFurnitures = mutableListOf<DefineFurniture>()
    var placeAvatars = mutableListOf<PlaceAvatar>()
    var defineAvatars = mutableListOf<DefineAvatar>()
    var placePets = mutableListOf<PlacePet>()
    var definePets = mutableListOf<DefinePet>()
    var placeActionItems = mutableListOf<PlaceActionItem>()

    override fun readFrom(buffer: ByteBuilder) {
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

            placeActionItem.itemCode = buffer.readString()
            placeActionItem.itemType = buffer.readString()
            placeActionItem.sequence = buffer.readInt()
            placeActionItem.ownerCode = buffer.readString()
            placeActionItem.x = buffer.readShort()
            placeActionItem.y = buffer.readShort()
            placeActionItem.z = buffer.readShort()
            placeActionItem.actionItemType = 2

            this.placeActionItems.add(placeActionItem)
        }

        val loc9 = buffer.readInt()

        this.isAdmin = (loc9 and 1) > 0
        this.isPatrol = (loc9 and 2) > 0
        this.c = buffer.readBoolean()
        this.serverTime = buffer.readDouble()
        this.isRefleshedCosmeItem = buffer.readBoolean()
        this.isAllowRoomChange = buffer.readBoolean()

        val loc11 = mutableMapOf<String, String>()
        for(i in 1..buffer.readByte()){
            val loc10 = buffer.readString()

            loc11[loc10] = loc10
        }

        this.defineAvatars.filter { loc11[it.characterId] != null }.forEach { it.friend = true }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}