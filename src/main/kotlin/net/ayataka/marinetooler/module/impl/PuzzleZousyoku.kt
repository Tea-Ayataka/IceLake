package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.ProgressPuzzlePacket

object PuzzleZousyoku : Module() {
    @Volatile
    var amount = 100_000

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is ProgressPuzzlePacket) {
            packet.point *= 50
        }
    }
}