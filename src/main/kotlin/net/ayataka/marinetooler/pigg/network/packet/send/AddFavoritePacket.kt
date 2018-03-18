package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class AddFavoritePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.ADD_FAVORITE.id
    override val encrypted = true

    var userCode = ""
    var timeStamp: Double = 0.0
    var flag = false

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString()
        timeStamp = buffer.readDouble()
        flag = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(userCode)
        buffer.writeRawDouble(timeStamp)
        //buffer.writeDoubleTimeStamp()
        buffer.writeBoolean(flag)

        return buffer
    }
}