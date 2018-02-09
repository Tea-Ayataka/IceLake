package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ChangeWindowAquariumPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.CHANGE_WINDOW_AQUARIUM.id

    var method = ""
    var data = ByteArray(0)

    override fun readFrom(buffer: ByteBuilder) {
        this.method = buffer.readString()
        this.data = buffer.readAllBytes()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.method)
        buffer.writeRawBytes(this.data)

        return buffer
    }
}