package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class TalkResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.TALK_RESULT.id

    var usercode = ""
    var message = ""
    var nickName = ""
    var color = 0xFFFFFF
    var amebaId = ""
    var ballonColor = 0xFFFFFF
    var roomCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        usercode = buffer.readString(16)
        message = buffer.readString()
        color = buffer.readInt()
        amebaId = buffer.readString()
        nickName = buffer.readString()
        ballonColor = buffer.readInt()
        roomCode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer
                .writeRawString(usercode)
                .writeString(message)
                .writeInt(color)
                .writeString(amebaId)
                .writeString(nickName)
                .writeInt(ballonColor)
                .writeString(roomCode)

        return buffer
    }
}