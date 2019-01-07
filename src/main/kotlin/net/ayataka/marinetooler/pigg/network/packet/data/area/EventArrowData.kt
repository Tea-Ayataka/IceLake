package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class EventArrowData : PacketData {
    var swfCode = ""
    var eventAreaMessage = ""
    var category = ""
    var subCategoryCode = ""
    var startTime = 0.0
    var endTime = 0.0

    override fun readFrom(buffer: ByteBuilder) {
        swfCode = buffer.readString()
        eventAreaMessage = buffer.readString()
        category = buffer.readString()
        subCategoryCode = buffer.readString()
        startTime = buffer.readDouble()
        endTime = buffer.readDouble()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(swfCode, eventAreaMessage, category, subCategoryCode)
                .writeDouble(startTime, endTime)
    }
}