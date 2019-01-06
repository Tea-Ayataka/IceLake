package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class LeaveUser : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.LEAVE_USER.id

    var userCode = ""
    var areaCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString(16)
        areaCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawString(userCode)
        buffer.writeString(areaCode)

        return buffer
    }
}