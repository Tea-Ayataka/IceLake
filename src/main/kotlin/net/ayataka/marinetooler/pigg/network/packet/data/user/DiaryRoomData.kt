package net.ayataka.marinetooler.pigg.network.packet.data.user

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class DiaryRoomData : PacketData {
    var isDiaryIconShow = false
    var isDiaryReadEnable = false
    var isDiaryNewPage = false
    var totalDiaryPage = 0

    override fun readFrom(buffer: ByteBuilder) {
        isDiaryIconShow = buffer.readBoolean()
        isDiaryReadEnable = buffer.readBoolean()
        isDiaryNewPage = buffer.readBoolean()
        totalDiaryPage = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeBoolean(isDiaryIconShow, isDiaryReadEnable, isDiaryNewPage)
                .writeInt(totalDiaryPage)
    }
}