package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.send.*

object NoticeSpammer : Module() {

    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is AddFavoritePacket) {
            event.packet.canceled = true

            val target = packet.userCode

            val noticePacket = NotifyUserRoomEnteredPacket()
            noticePacket.userCode = target
            Pigg.send(noticePacket)
        }
    }
}