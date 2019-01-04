package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.TalkResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ChannelFloorUpdatePlayListRequest
import net.ayataka.marinetooler.utils.web.WebClient
import java.awt.Color

object PlaylistModifer : Module() {
    var videoID = ""
    var title = ""

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is ChannelFloorUpdatePlayListRequest && packet.type == 1) {
            packet.duration = 180
            packet.videoID = videoID
            packet.videoTitle = title

            enabled = false
        }
    }
}