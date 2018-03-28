package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class CheckBanWordResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.CHECK_BAN_WORD_RESULT.id

    var isBan = false
    var message = ""

    override fun readFrom(buffer: ByteBuilder) {
        isBan = buffer.readBoolean()
        message = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeBoolean(isBan)
        buffer.writeString(message)
        return buffer
    }
}