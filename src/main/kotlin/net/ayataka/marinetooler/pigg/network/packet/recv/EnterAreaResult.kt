package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData

class EnterAreaResult : BaseAreaData() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ENTER_AREA_RESULT.id


    override fun readFrom(buffer: ByteBuilder) {
        super.readFrom(buffer)


    }
}