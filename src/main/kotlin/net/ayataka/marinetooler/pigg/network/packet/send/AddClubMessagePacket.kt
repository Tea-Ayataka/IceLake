package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

class AddClubMessagePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.ADD_CLUB_MESSAGE.id

    var areaCode = ""
    var msg = ""

    override fun readFrom(buffer: ByteBuilder) {
        areaCode = buffer.readString()
        msg = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(areaCode)
        buffer.writeString(msg)

        return buffer
    }
}