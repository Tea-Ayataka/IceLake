package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.AlertResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.ErrorPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginChatResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ChannelFloorUpdatePlayListRequest
import net.ayataka.marinetooler.pigg.network.packet.send.IceAreaPacket

object IceAreaConnector : Module() {
    var areaData = BaseAreaData()

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is AlertResultPacket && packet.message == "810") {
            packet.canceled = true

            Pigg.send(IceAreaPacket().apply {
                avatarData = areaData.defineAvatars.find { it.data.userCode == CurrentUser.usercode }?.data!!
            })
        }
    }
}