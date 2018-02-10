package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ListAreaTopResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_AREA_TOP_RESULT.id

    var text = ""
    var url = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.text = buffer.readString()
        this.url = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.text)
        buffer.writeString(this.url)

        return buffer
    }
}