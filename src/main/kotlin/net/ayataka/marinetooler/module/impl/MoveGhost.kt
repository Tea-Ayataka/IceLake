package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.network.packet.recv.MoveResultPacket
import net.ayataka.marinetooler.utils.runLater

object MoveGhost : Module() {
    var directions = hashMapOf<String, Byte>()

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is MoveResultPacket) {
            if (packet.usercode == Tooler.targetUser) {
                runLater({ CurrentUser.move(packet.x.toInt(), packet.y.toInt(), packet.z.toInt()) }, 200)
            }
        }
    }
}