package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData

class EnterUserGardenResult : BaseAreaData() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.ENTER_USER_GARDEN_RESULT.id
}