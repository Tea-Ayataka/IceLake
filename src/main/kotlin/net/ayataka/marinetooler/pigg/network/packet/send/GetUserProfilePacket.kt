package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetUserProfilePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_USER_PROFILE.id

    var usercode = String()

    override fun readFrom(buffer: ByteBuilder) {
        this.usercode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.usercode)
        return buffer
    }
}