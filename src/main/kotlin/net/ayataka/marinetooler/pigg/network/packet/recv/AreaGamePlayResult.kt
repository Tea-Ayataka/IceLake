package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class AreaGamePlayResult : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.AREA_GAME_PLAY_RESULT.id

    var id: Short = 0
    var data: ByteArray? = null

    override fun readFrom(buffer: ByteBuilder) {
        id = buffer.readShort()

        if (buffer.array().size - buffer.getPos() > 0) {
            data = buffer.readBytes(buffer.array().size - buffer.getPos())
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeShort(id)
        data?.let { buffer.writeRawBytes(it) }
        return buffer
    }
}