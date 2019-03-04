package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.AddClubMessagePacket
import net.ayataka.marinetooler.pigg.network.packet.send.CheckContributeClubFurniturePacket
import net.ayataka.marinetooler.pigg.network.packet.send.ContributeClubFurniturePacket

object InstantDonator : Module() {
    private var message = "てすと"

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is CheckContributeClubFurniturePacket) {
            val donatePacket = ContributeClubFurniturePacket()
            donatePacket.areaCode = packet.areaCode
            donatePacket.furnitureId = packet.furnitureId
            donatePacket.message = message

            event.canceled = true
            PiggProxy.send(donatePacket)

            // Pigg.receive(CheckContributeClubFurnitureResult())
        } else if (packet is AddClubMessagePacket) {
            event.canceled = true

            message = packet.msg
        }
    }
}