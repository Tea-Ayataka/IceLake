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
    }

    fun writeTo(buffer: ByteBuilder) : ByteBuilder{
        return buffer
                .writeString(this.categoryCode)
                .writeString(this.categoryName)
                .writeString(this.areaCode)
                .writeString(this.areaName)

                .writeString(this.frontCode)
                .writeString(this.wallCode)
                .writeString(this.floorCode)
                .writeString(this.windowCode)

                .writeRawShort(this.sizeX)
                .writeRawShort(this.sizeY)
    }
}