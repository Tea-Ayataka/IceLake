package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData

class EnterAreaResult : BaseAreaData() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ENTER_AREA_RESULT.id

    var areaGameId: Byte = 0
    var loc2 = 0
    var isPiggDomeOpen = false

    override fun readFrom(buffer: ByteBuilder) {
        super.readFrom(buffer)

        areaGameId = buffer.readByte()
        loc2 = buffer.readInt()
        isPiggDomeOpen = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        if(loc2 != 0){
            return null
        }

        super.writeTo(buffer)

        buffer.writeByte(areaGameId)
                .writeInt(loc2)
                .writeBoolean(isPiggDomeOpen)

        return buffer
    }
}