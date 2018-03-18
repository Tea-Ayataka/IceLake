package net.ayataka.marinetooler.pigg.network

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

// Pigg Protocol Implementation
object Protocol {
    // PacketID:PacketClass
    private val packets = HashMap<ServerType, HashMap<Short, KClass<out Packet>>>()

    // Packet Cipher Keys
    val cipherKey = HashMap<ServerType, ByteArray>()

    init {
        ServerType.values().forEach { packets[it] = HashMap() } // Init HashMap

        // Register packets
        // SEND (Client bound)
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

        // RECV (Server bound)
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
    }

    private fun register(clazz: KClass<out Packet>) {
        val instance = clazz.java.newInstance()
        packets[instance.server]!![instance.packetId] = clazz
    }

    fun convert(rawBuffer: ByteBuffer, type: ServerType): Packet? {
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
        dump(rawBuffer.array().toHexString())
        dump(String(rawBuffer.array()))

        // Find packet handler
        val packet = packets[type]!![id]?.java?.newInstance() ?: return null

        try {
            packet.read(buffer)
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
            cipherKey[type] = buffer.skip(4).readRawBytes(4).plus(buffer.reset().skip(2).readRawBytes(4))
            dump("$type SERVER Decrypt key : ${cipherKey[type]?.toHexString()}")
        }
    }
}

