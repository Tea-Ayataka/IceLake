package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class TableGameResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.TABLE_GAME_RESULT.id

    var method = ""
    var serial = false
    var data: ByteArray? = null

    override fun readFrom(buffer: ByteBuilder) {
        method = buffer.readString()
        serial = buffer.readBoolean()

        val hasData = buffer.readBoolean()

        if (hasData) {
            data = buffer.readAllBytes()
        }

        info(" METHOD IS ${method} $hasData")

        info(serial.toString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(method)
        buffer.writeBoolean(serial)

        buffer.writeBoolean(data != null)

        data?.let { buffer.writeRawBytes(it) }

        return buffer
    }
}