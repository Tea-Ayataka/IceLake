package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
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
        this.code = buffer.readString()
        this.message = buffer.readString()
        this.exceptionClass = buffer.readString()
        this.exceptionTrace = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
       // buffer.writeString(this.code)
       // buffer.writeString(this.message)
       // buffer.writeString(this.exceptionClass)
       // buffer.writeString(this.exceptionTrace)
        return null
    }
}