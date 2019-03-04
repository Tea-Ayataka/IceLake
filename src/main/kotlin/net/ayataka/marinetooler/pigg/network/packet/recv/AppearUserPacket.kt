package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData

class AppearUserPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.APPEAR_USER.id

    var avatarData = AvatarData()
    var areaCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        avatarData.readFrom(buffer)
        areaCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        avatarData.writeTo(buffer)
        buffer.writeString(areaCode)

        return buffer
    }
}