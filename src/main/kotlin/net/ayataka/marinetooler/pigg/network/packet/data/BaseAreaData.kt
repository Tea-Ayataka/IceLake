package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.readUCodesFromAreaPacket

open class BaseAreaData : Packet() {
    override val server = ServerType.CHAT
    override val packetId = InfoPacketID.NONE.id

    var users = mutableListOf<String>()

    var categoryCode: String = ""
    var categoryName: String = ""
    var areaCode: String = ""
    var areaName: String = ""
    var frontCode: String = ""
    var wallCode: String = ""
    var floorCode: String = ""
    var windowCode: String = ""
    var sizeX: Short = 0
    var sizeY: Short = 0

    override fun readFrom(buffer: ByteBuilder) {
        this.categoryCode = buffer.readString()
        this.categoryName = buffer.readString()
        this.areaCode = buffer.readString()
        this.areaName = buffer.readString()

        this.frontCode = buffer.readString()
        this.wallCode = buffer.readString()
        this.floorCode = buffer.readString()
        this.windowCode = buffer.readString()

        this.sizeX = buffer.readShort()
        this.sizeY = buffer.readShort()

        // Get area users
        this.users = readUCodesFromAreaPacket(buffer)
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}