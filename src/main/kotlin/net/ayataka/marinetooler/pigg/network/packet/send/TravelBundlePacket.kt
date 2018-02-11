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
        this.categoryCode = buffer.readString()
        this.areaCode = buffer.readString()
        this.flag = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.categoryCode)
        buffer.writeString(this.areaCode)
        buffer.writeBoolean(this.flag)

        return buffer
    }
}