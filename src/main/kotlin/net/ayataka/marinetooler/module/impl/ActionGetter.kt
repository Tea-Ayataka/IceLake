package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.TalkResultPacket
import java.awt.Color

object ActionGetter : Module() {
    private val color = Color(255, 150, 253)

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is ActionResultPacket) {
            val talkPacket = TalkResultPacket()

            talkPacket.usercode = packet.usercode
            talkPacket.message = packet.actionCode
            talkPacket.amebaId = "aobanomankprpr0202"
            talkPacket.nickName = "Action"
            talkPacket.roomCode = CurrentUser.areaData.areaData.areaCode

            talkPacket.color = color.rgb

            Pigg.receive(talkPacket)
        }
    }
}