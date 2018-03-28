package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class PlaceActionItem : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.PLACE_ACTION_ITEM.id
    override val encrypted = true

    var code = ""
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0

    override fun readFrom(buffer: ByteBuilder) {
        code = buffer.readString()
        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()

        dump("PLACEACTION: $code")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(code)
        buffer.writeShort(x, y, z)
        buffer.writeDoubleTimeStamp()
        return buffer
    }
}