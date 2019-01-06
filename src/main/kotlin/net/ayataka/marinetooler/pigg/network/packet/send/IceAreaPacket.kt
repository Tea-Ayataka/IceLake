package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefinePet
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.toHexString

class IceAreaPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ICEAREA.id

    var avatarData = AvatarData()

    override fun readFrom(buffer: ByteBuilder) {
        avatarData.readFrom(buffer)
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        avatarData.writeTo(buffer)

        return buffer
    }
}