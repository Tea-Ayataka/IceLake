package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.area.*
import net.ayataka.marinetooler.pigg.network.packet.data.user.DiaryRoomData

class EnterUserRoomResult : BaseAreaData() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ENTER_USER_ROOM_RESULT.id

    var roomIndex = 0
    var expansions = mutableListOf<Expansion>()
    var petSolveFurniturePlaceDatas = mutableListOf<PetSolveFurniturePlaceData>()
    var ownerData = AreaOwnerData()
    var diaryRoomData = DiaryRoomData()
    var eventArrowData: EventArrowData? = null

    var isPiggLifeAvailable: Byte = 0
    var isPiggIslandAvailable: Byte = 0
    var isPiggCafeAvailable: Byte = 0
    var isPiggWorldAvailable: Byte = 0
    var isPiggBraveAvailable: Byte = 0

    var isCheckPlaceGift = false
    var isGroupMessageEnabled = false
    var canMoveGarden = false
    var oneMessage = ""
    var becomableFriend = false
    var defaultOwnerAreaCode = ""
    var defaultVisitorAreaCode = ""
    var contestCode: String? = null
    var enablePetSolveFurniture = false
    var allowMannequinDetail: Byte = 0
    var mannequinSize: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        super.readFrom(buffer)

        roomIndex = buffer.readInt()

        (0 until buffer.readInt()).forEach {
            expansions.add(Expansion().apply {
                readFrom(buffer)
            })
        }

        (0 until buffer.readInt()).forEach {
            expansions.add(Expansion().apply {
                readFrom(buffer)

                type = 1
            })
        }

        ownerData.readFrom(buffer)

        isPiggLifeAvailable = buffer.readByte()
        isPiggIslandAvailable = buffer.readByte()
        isPiggCafeAvailable = buffer.readByte()
        isPiggWorldAvailable = buffer.readByte()
        isPiggBraveAvailable = buffer.readByte()

        isCheckPlaceGift = buffer.readBoolean()
        isGroupMessageEnabled = buffer.readBoolean()
        canMoveGarden = buffer.readBoolean()
        oneMessage = buffer.readString()

        if(buffer.readBoolean()){
            eventArrowData = EventArrowData().apply {
                readFrom(buffer)
            }
        }

        becomableFriend = buffer.readBoolean()
        defaultOwnerAreaCode = buffer.readString()
        defaultVisitorAreaCode = buffer.readString()

        diaryRoomData.readFrom(buffer)

        if(buffer.readBoolean()){
            contestCode = buffer.readString()
        }

        (0 until buffer.readInt()).forEach {
            petSolveFurniturePlaceDatas.add(PetSolveFurniturePlaceData().apply {
                readFrom(buffer)
            })
        }

        enablePetSolveFurniture = buffer.readBoolean()
        allowMannequinDetail = buffer.readByte()

        mannequinSize = buffer.readByte()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        super.writeTo(buffer)

        buffer.writeInt(roomIndex, expansions.filter { it.type == 0 }.size)

        expansions.filter { it.type == 0 }.forEach {
            it.writeTo(buffer)
        }

        buffer.writeInt(expansions.filter { it.type == 1 }.size)

        expansions.filter { it.type == 1 }.forEach {
            it.writeTo(buffer)
        }

        ownerData.writeTo(buffer)
        buffer.writeByte(isPiggLifeAvailable, isPiggIslandAvailable, isPiggCafeAvailable, isPiggWorldAvailable, isPiggBraveAvailable)
                .writeBoolean(isCheckPlaceGift, isGroupMessageEnabled, canMoveGarden)
                .writeString(oneMessage)
                .writeBoolean(eventArrowData != null)

        eventArrowData?.writeTo(buffer)

        buffer.writeBoolean(becomableFriend)
                .writeString(defaultOwnerAreaCode, defaultVisitorAreaCode)

        diaryRoomData.writeTo(buffer)

        buffer.writeBoolean(contestCode != null)

        contestCode?.let { buffer.writeString(it) }

        buffer.writeInt(petSolveFurniturePlaceDatas.size)
        petSolveFurniturePlaceDatas.forEach {
            it.writeTo(buffer)
        }

        buffer.writeBoolean(enablePetSolveFurniture)
                .writeByte(allowMannequinDetail)
                .writeByte(mannequinSize)

        return buffer
    }
}