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
    var description = ""
    var goodCount = 0
    var friendCount = 0
    var isFriend = false
    //？
    var ignore = false
    var sameArea = false

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString()
        amebaId = buffer.readString()
        buffer.readString()
        nickName = buffer.readString()
        description = buffer.readString()
        goodCount = buffer.readInt()
        friendCount = buffer.readInt()
        isFriend = buffer.readBoolean()
        ignore = buffer.readBoolean()
        sameArea = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}