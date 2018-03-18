package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

open class ListUserItemResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_USER_ITEM_RESULT.id

    var max = 0
    var size = 0

    var data = ByteArray(0)
    override fun readFrom(buffer: ByteBuilder) {
        max = buffer.readInt()
        size = buffer.readInt()

        data = buffer.readAllBytes()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawInt(max)
        buffer.writeRawInt(size)

        buffer.writeRawBytes(data)

        return buffer
    }
}