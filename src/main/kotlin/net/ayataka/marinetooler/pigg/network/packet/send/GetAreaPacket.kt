package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetAreaPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_AREA.id
    override val encrypted = true

    var category = ""
    var code = ""
    var targetUser = false
    var isShuffleGarden = false

    override fun readFrom(buffer: ByteBuilder) {
        category = buffer.readString()
        code = buffer.readString()
        targetUser = buffer.readBoolean()
        isShuffleGarden = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(category)
        buffer.writeString(code)
        buffer.writeBoolean(targetUser)
        buffer.writeBoolean(isShuffleGarden)
        buffer.writeDoubleTimeStamp()
        return buffer
    }
}