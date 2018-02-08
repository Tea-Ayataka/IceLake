package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket

object ActionGhost : Module() {
    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is ActionResultPacket) {
            if (packet.usercode == Tooler.targetUser) {
                CurrentUser.playAction(packet.actionCode)
            }
        }
    }
}