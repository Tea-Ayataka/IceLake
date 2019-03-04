package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class TravelBundlePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.TRAVEL_BUNDLE.id

    var categoryCode = ""
    var areaCode = ""
    var flag = false

    override fun readFrom(buffer: ByteBuilder) {
        categoryCode = buffer.readString()
        areaCode = buffer.readString()
        flag = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(categoryCode)
        buffer.writeString(areaCode)
        buffer.writeBoolean(flag)

        return buffer
    }
}