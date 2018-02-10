package net.ayataka.marinetooler.module.impl

import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.send.NotifyUserRoomEnteredPacket

object NoticeSpammer : Module() {

    fun spam(usercode: String){
        val packet1 = NotifyUserRoomEnteredPacket()

        packet1.userCode = usercode

        Pigg.send(packet1)
    }
}