package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.GetUserProfilePacket
import net.ayataka.marinetooler.pigg.network.packet.send.GoodPiggPacket
import net.ayataka.marinetooler.utils.info

object FishMacro : Module() {

    fun start(){

    }

    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet
    }

    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is GetUserProfilePacket) {

        }
    }
}