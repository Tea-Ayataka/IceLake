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
        ServerType.values().forEach { this.packets[it] = HashMap() } // Init HashMap

        // Register packets
        // SEND (Client bound)
        this.register(GetShopPacket::class)
        this.register(ClickPiggShopItemPacket::class)
        this.register(ProceedTutorialPacket::class)
        this.register(GetAreaPacket::class)
        this.register(TableGamePacket::class)
        this.register(EnterRoomPacket::class)
        this.register(AreaGamePlayPacket::class)
        this.register(SystemActionPacket::class)
        this.register(RoomActionPacket::class)
        this.register(PresentMyItemGiftPacket::class)
        this.register(GoodPiggPacket::class)
        this.register(ActionPacket::class)
        this.register(CancelTypingPacket::class)
        this.register(TalkPacket::class)
        this.register(MovePacket::class)
        this.register(MoveEndPacket::class)
        this.register(GetUserProfilePacket::class)
        this.register(GetDiaryPacket::class)
        this.register(ChangeWindowAquariumPacket::class)

        // RECV (Server bound)
        this.register(ActionResultPacket::class)
        this.register(TalkResultPacket::class)
        this.register(MoveResultPacket::class)
        this.register(MoveEndResultPacket::class)
        this.register(AppearUserPacket::class)
        this.register(ErrorPacket::class)
        this.register(EnterAreaResult::class)
        this.register(GetUserProfileResultPacket::class)
        this.register(EnterUserGardenResult::class)
        this.register(EnterUserRoomResult::class)
        this.register(GetAreaResultPacket::class)
        this.register(AlertResultPacket::class)
        this.register(LoginChatResultPacket::class)
    }

    private fun register(clazz: KClass<out Packet>) {
        val instance = clazz.java.newInstance()
        this.packets[instance.server]!![instance.packetId] = clazz
    }

    fun convert(rawBuffer: ByteBuffer, type: ServerType): Packet? {
        if (rawBuffer.array().size > 1024 * 128 || rawBuffer.array().size < 6)
            return null

        val buffer = ByteBuilder(rawBuffer)

        // Get header
        val header = buffer.readShort()
        if (header != HeaderID.COMMAND.id) {
            this.handleSpecialPacket(header, buffer, type)
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
        val packet = this.packets[type]!![id]?.java?.newInstance() ?: return null

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
            this.cipherKey[type] = buffer.skip(4).readRawBytes(4).plus(buffer.reset().skip(2).readRawBytes(4))
            dump("$type SERVER Decrypt key : ${this.cipherKey[type]?.toHexString()}")
        }
    }
}

