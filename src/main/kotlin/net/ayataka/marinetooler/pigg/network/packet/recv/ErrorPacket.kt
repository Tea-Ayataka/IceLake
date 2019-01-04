package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ErrorPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.ERROR.id

    var code = ""
    var message = ""
    var exceptionClass = ""
    var exceptionTrace = ""

    override fun readFrom(buffer: ByteBuilder) {
        code = buffer.readString()
        message = buffer.readString()
        exceptionClass = buffer.readString()
        exceptionTrace = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
       buffer.writeString(code)
       buffer.writeString(message)
       buffer.writeString(exceptionClass)
       buffer.writeString(exceptionTrace)

        return null
    }
}