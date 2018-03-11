package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.AddClubMessagePacket
import net.ayataka.marinetooler.pigg.network.packet.send.CheckContributeClubFurniturePacket
import net.ayataka.marinetooler.pigg.network.packet.send.ContributeClubFurniturePacket

object InstantDonator : Module() {
    private var msg: String? = "てすと"
    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is CheckContributeClubFurniturePacket) {
            val donatePacket = ContributeClubFurniturePacket()
            donatePacket.areaCode = packet.areaCode
            donatePacket.furnitureId = packet.furnitureId
            donatePacket.message = this.msg!!
            event.packet = donatePacket
            // Pigg.receive(CheckContributeClubFurnitureResult())
        }
        else if(packet is AddClubMessagePacket){
            packet.canceled = true

            this.msg = packet.msg
        }
    }
}