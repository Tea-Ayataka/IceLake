package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class TableGamePacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.TABLE_GAME.id

    var method = ""
    var data: ByteArray? = null

    override fun readFrom(buffer: ByteBuilder) {
        this.method = buffer.readString()
        val hasData = buffer.readBoolean()

        if (hasData) {
            this.data = buffer.readAllBytes()
        }

        info(" METHOD IS ${this.method} $hasData")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.method)
        buffer.writeBoolean(this.data != null)
        this.data?.let { buffer.writeRawBytes(it) }
        return buffer
    }
}