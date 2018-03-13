package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.readUCodesFromAreaPacket

open class BaseAreaData : Packet() {
    override val server = ServerType.CHAT
    override val packetId = InfoPacketID.NONE.id

    var users = mutableListOf<String>()

    lateinit var areaData: AreaData
    override fun readFrom(buffer: ByteBuilder) {
        this.areaData = AreaData()

        this.areaData.readFrom(buffer)

        // Get area users
        this.users = readUCodesFromAreaPacket(buffer)
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeRawBytes(this.areaData.writeTo().build().array())

        return null
    }
}