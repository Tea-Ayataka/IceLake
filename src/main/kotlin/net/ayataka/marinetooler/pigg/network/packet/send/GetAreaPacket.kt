package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class GetAreaPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_AREA.id
    override val encrypted = true

    var category = ""
    var code = ""
    var targetUser = false
    var isShuffleGarden = false

    override fun readFrom(buffer: ByteBuilder) {
        this.category = buffer.readString()
        this.code = buffer.readString()
        this.targetUser = buffer.readBoolean()
        this.isShuffleGarden = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.category)
        buffer.writeString(this.code)
        buffer.writeBoolean(this.targetUser)
        buffer.writeBoolean(this.isShuffleGarden)
        buffer.writeDoubleTimeStamp()
        return buffer
    }
}