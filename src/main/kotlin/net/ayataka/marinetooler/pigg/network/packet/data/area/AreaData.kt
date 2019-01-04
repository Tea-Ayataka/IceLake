package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class AreaData : PacketData {
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
        categoryCode = buffer.readString()
        categoryName = buffer.readString()
        areaCode = buffer.readString()
        areaName = buffer.readString()

        frontCode = buffer.readString()
        wallCode = buffer.readString()
        floorCode = buffer.readString()
        windowCode = buffer.readString()

        sizeX = buffer.readShort()
        sizeY = buffer.readShort()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(categoryCode)
                .writeString(categoryName)
                .writeString(areaCode)
                .writeString(areaName)

                .writeString(frontCode)
                .writeString(wallCode)
                .writeString(floorCode)
                .writeString(windowCode)

                .writeShort(sizeX)
                .writeShort(sizeY)
    }

    override fun toString(): String {
        return "AreaData(categoryCode='$categoryCode', categoryName='$categoryName', areaCode='$areaCode', areaName='$areaName', frontCode='$frontCode', wallCode='$wallCode', floorCode='$floorCode', windowCode='$windowCode', sizeX=$sizeX, sizeY=$sizeY)"
    }
}