package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.TalkResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.TalkPacket

object ChatGhost : Module() {
    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is TalkResultPacket) {
            if (packet.usercode == Tooler.targetUser) {
                this.talk(packet.message)
            }
        }
    }

    private fun talk(msg: String) {
        val packet = TalkPacket()
        packet.text = msg
        Pigg.send(packet)
    }
}