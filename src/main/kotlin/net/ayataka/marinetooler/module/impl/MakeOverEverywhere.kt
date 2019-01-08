package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.EnterAreaResult
import net.ayataka.marinetooler.pigg.network.packet.recv.MoveFurnitureResult
import net.ayataka.marinetooler.pigg.network.packet.recv.RemoveFurnitureResult
import net.ayataka.marinetooler.pigg.network.packet.send.MoveFurniture
import net.ayataka.marinetooler.pigg.network.packet.send.RemoveFurniture

object MakeOverEverywhere : Module() {
    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is EnterAreaResult) {
            packet.meta = 1
        }
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (CurrentUser.areaData.areaData.categoryCode != "user") {
            if(packet is MoveFurniture) {
                packet.canceled = true
                Pigg.receive(MoveFurnitureResult().apply {
                    direction = packet.direction
                    sequence = packet.sequence
                    x = packet.x
                    y = packet.y
                    z = packet.z
                })
            }

            if(packet is RemoveFurniture) {
                packet.canceled = true
                Pigg.receive(RemoveFurnitureResult().apply {
                    sequence = packet.sequence
                })
            }
        }
    }
}