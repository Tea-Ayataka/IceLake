package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.network.packet.data.UserItemData
import net.ayataka.marinetooler.pigg.network.packet.send.PresentMyItemGiftPacket

object FreeItemSender : Module() {
    fun give() {
        val packet = PresentMyItemGiftPacket()
        packet.usercode = "3690dec26e58d7fb"
        packet.message = "fack you"
        packet.items

        Pigg.send(packet)
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {

    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is PresentMyItemGiftPacket) {
            packet.items.add(UserItemData("casino_shopitem141017_escargot_1410", "item", 0, 1))
            event.packet = packet
        }
    }
}