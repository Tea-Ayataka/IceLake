package net.ayataka.marinetooler.pigg.network

import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.HeaderID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.utils.warn
import java.nio.ByteBuffer
import kotlin.reflect.KClass

// Piggプロトコルの実装
// ByteArray -> Packet へ変換する。必要であれば複合化も行う。
class Protocol {
    // PacketID:PacketClass
    private val packets = HashMap<ServerType, HashMap<Short, KClass<out Packet>>>()

    // Packet Cipher Keys
    val connectionId = HashMap<ServerType, Int>()
    val cipherKey = HashMap<ServerType, ByteArray>()

    init {
        ServerType.values().forEach { packets[it] = HashMap() } // Init HashMap

        // Register packets
        // SEND (Client bound)
        register(PlaceActionItem::class)
        register(OneMessageSavePacket::class)
        register(LoginPacket::class)
        register(MoveFurniture::class)
        register(PlaceFurniture::class)
        register(PlayGachaStepupPacket::class)
        register(GetPiggShopCategory::class)
        register(TravelBundlePacket::class)
        register(GetShopPacket::class)
        register(ClickPiggShopItemPacket::class)
        register(ProceedTutorialPacket::class)
        register(GetAreaPacket::class)
        register(TableGamePacket::class)
        register(EnterRoomPacket::class)
        register(AreaGamePlayPacket::class)
        register(SystemActionPacket::class)
        register(RoomActionPacket::class)
        register(PresentMyItemGiftPacket::class)
        register(GoodPiggPacket::class)
        register(ActionPacket::class)
        register(CancelTypingPacket::class)
        register(TalkPacket::class)
        register(MovePacket::class)
        register(MoveEndPacket::class)
        register(GetUserProfilePacket::class)
        register(GetDiaryPacket::class)
        register(ChangeWindowAquariumPacket::class)
        register(NotifyUserRoomEnteredPacket::class)
        register(GetNoticeBoardMessageOfAreaPacket::class)
        register(AddFavoritePacket::class)
        register(ProgressPuzzlePacket::class)
        register(CheckContributeClubFurniturePacket::class)
        register(ContributeClubFurniturePacket::class)
        register(AddClubMessagePacket::class)
        register(GetSnapshotToken::class)
        register(ListActionPacket::class)
        register(AreaGameJoinPacket::class)
        register(AreaGameLeavePacket::class)
        register(LoginChatPacket::class)
        register(PetMove::class)
        register(PetMoveEnd::class)
        register(GetPetProfile::class)
        register(SetPetProfile::class)

        // RECV (Server bound)
        register(CheckBanWordResultPacket::class)
        register(LoginResultPacket::class)
        register(RemoveFurniture::class)
        register(GetPiggShopGachaResultPacket::class)
        register(ActionResultPacket::class)
        register(TalkResultPacket::class)
        register(MoveResultPacket::class)
        register(MoveEndResultPacket::class)
        register(AppearUserPacket::class)
        register(ErrorPacket::class)
        register(EnterAreaResult::class)
        register(GetUserProfileResultPacket::class)
        register(EnterUserGardenResult::class)
        register(EnterUserRoomResult::class)
        register(GetAreaResultPacket::class)
        register(AlertResultPacket::class)
        register(LoginChatResultPacket::class)
        register(ListAreaTopResultPacket::class)
        register(TableGameResultPacket::class)
        register(ListUserItemResultPacket::class)
        register(ListUserFurnitureResultPacket::class)
        register(GetSnapshotTokenResult::class)
        register(AreaGamePlayResult::class)
        register(ProgressPuzzleResultPacket::class)
        register(GetPuzzleUserStatusResult::class)
        register(FinishDressupResult::class)
        register(RoomActionResult::class)
        register(ListActionResult::class)
    }

    private fun register(clazz: KClass<out Packet>) {
        val instance = clazz.java.newInstance()
        packets[instance.server]!![instance.packetId] = clazz
    }

    fun convert(rawBuffer: ByteBuffer, type: ServerType, direction: PacketDirection): Packet? {
        if (rawBuffer.array().size > 1024 * 128 || rawBuffer.array().size < 6)
            return null

        val buffer = ByteBuilder(rawBuffer)

        // Get header
        val header = buffer.readShort()
        if (header != HeaderID.COMMAND.id) {
            handleSpecialPacket(header, buffer, type)
            return null
        }

        // Get packet id (command)
        val id = buffer.skip(4).readShort()

        val packetID = when (type) {
            ServerType.INFO -> {
                InfoPacketID.values().find { it.id == id }
            }
            ServerType.CHAT -> {
                ChatPacketID.values().find { it.id == id }
            }
        }

        if (packetID == null) {
            dump("UNKNOWN PACKET ID : $id")
            dump(rawBuffer.array().toHexString())
            dump(String(rawBuffer.array()))
            return null
        }

        // Dump data
        dump("PACKET ID : $packetID ($type)")
        dump(String(rawBuffer.array()))

        ICE_LAKE.mainWindow?.recordPacket(direction, type.name, packetID.name, rawBuffer.array())

        // Find packet handler
        val packet = packets[type]!![id]?.java?.newInstance() ?: return null

        try {
            packet.read(buffer, cipherKey[type])
        } catch (ex: Exception) {
            warn("FAILED TO CONVERT PACKET!")
            ex.printStackTrace()
            return null
        }

        dump("PACKET IS ${packet::class.java.simpleName}")
        return packet
    }

    private fun handleSpecialPacket(header: Short, buffer: ByteBuilder, type: ServerType) {
        if (header == HeaderID.CIPHER_KEY.id) {
            connectionId[type] = buffer.readInt()
            cipherKey[type] = buffer.pos(buffer.getPos() - 4).skip(4).readBytes(4).plus(buffer.reset().skip(2).readBytes(4))
            dump("$type SERVER Decrypt key : ${cipherKey[type]?.toHexString()}, Connection ID: ${connectionId[type]}")
        }
    }
}

