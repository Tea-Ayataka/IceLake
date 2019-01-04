package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetAreaResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_AREA_RESULT.id

    var type = String() // "user"
    var userCode = String() // "c4be9c50d3ff8704"
    var chatServerUri = String() // "wss://chat06.pigg.ameba.jp:443/command"
    var protocol = String() // "ws"

    override fun readFrom(buffer: ByteBuilder) {
        type = buffer.readString()
        userCode = buffer.readString()
        chatServerUri = buffer.readString()
        protocol = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(type)
        buffer.writeString(userCode)
        buffer.writeString(chatServerUri)
        buffer.writeString(protocol)
        buffer.writeTimeStamp()
        return buffer
    }
}