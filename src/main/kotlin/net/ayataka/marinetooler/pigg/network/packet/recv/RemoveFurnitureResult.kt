package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class RemoveFurnitureResult : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.REMOVE_FURNITURE_RESULT.id

    var sequence = 0
    var characterId = ""
    var isMannequin = false

    override fun readFrom(buffer: ByteBuilder) {
        sequence = buffer.readInt()
        characterId = buffer.readString()
        isMannequin = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(sequence)
        buffer.writeString(characterId)
        buffer.writeBoolean(isMannequin)
        return buffer
    }
}