package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.network.packet.recv.MoveResultPacket
import net.ayataka.marinetooler.utils.runLater

object MoveGhost : Module() {
    var directions = hashMapOf<String, Byte>()

    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is MoveResultPacket) {
            if (packet.usercode == Tooler.targetUser) {
                runLater({ CurrentUser.move(packet.x, packet.y, packet.z) }, 200)
            }
        }
    }
}