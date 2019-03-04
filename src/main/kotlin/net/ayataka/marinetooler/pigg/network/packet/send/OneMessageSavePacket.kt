package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class OneMessageSavePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.ONE_MESSAGE_SAVE.id

    var text = ""

    override fun readFrom(buffer: ByteBuilder) {
        text = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(text)
        return buffer
    }
}