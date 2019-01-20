package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.MoveResultPacket

object MoveGhost : Module() {
    var directions = hashMapOf<String, Byte>()

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is MoveResultPacket) {
            if (packet.usercode == ICE_LAKE.targetUser) {
                CurrentUser.move(packet.x.toInt(), packet.y.toInt(), packet.z.toInt())
            }
        }
    }
}