package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.GetUserProfilePacket

object FishMacro : Module() {

    fun start(){

    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is GetUserProfilePacket) {

        }
    }
}