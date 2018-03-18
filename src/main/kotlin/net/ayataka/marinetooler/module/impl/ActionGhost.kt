package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket

object ActionGhost : Module() {
    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is ActionResultPacket) {
            if (packet.usercode == Tooler.targetUser) {
                CurrentUser.playAction(packet.actionCode)
            }
        }
    }
}