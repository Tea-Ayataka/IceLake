package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class AreaData {
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

    fun readFrom(buffer: ByteBuilder){
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

    fun writeTo(buffer: ByteBuilder) : ByteBuilder{
        return buffer
                .writeString(categoryCode)
                .writeString(categoryName)
                .writeString(areaCode)
                .writeString(areaName)

                .writeString(frontCode)
                .writeString(wallCode)
                .writeString(floorCode)
                .writeString(windowCode)

                .writeRawShort(sizeX)
                .writeRawShort(sizeY)
    }
}