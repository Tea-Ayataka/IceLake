package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.CheckContributeClubFurniturePacket
import net.ayataka.marinetooler.pigg.network.packet.send.ContributeClubFurniturePacket

object InstantGiver : Module() {
    @EventTarget
    fun onSendEvent(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is CheckContributeClubFurniturePacket) {
            packet.canceled = true

            val outputPac = ContributeClubFurniturePacket()

            outputPac.areaCode = packet.areaCode
            outputPac.furnitureId = packet.furnitureId
            outputPac.msg = "あげる"

            Pigg.send(outputPac)
        }
    }
}