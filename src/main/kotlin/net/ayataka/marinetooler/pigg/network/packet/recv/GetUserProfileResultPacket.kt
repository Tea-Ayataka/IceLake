package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetUserProfileResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_USER_PROFILE_RESULT.id

    var usercode = ""
    var amebaId = ""
    var nickName = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.usercode = buffer.readString()
        this.amebaId = buffer.readString()
        buffer.readString()
        this.nickName = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}