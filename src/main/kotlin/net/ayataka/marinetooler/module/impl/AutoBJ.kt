package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent

object AutoBJ : Module() {
    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet
    }
}