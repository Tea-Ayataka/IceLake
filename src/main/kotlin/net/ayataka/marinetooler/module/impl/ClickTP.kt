package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket

object ClickTP : Module() {
    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is MovePacket) {
            // クリック式瞬間移動
            val end = MoveEndPacket()
            end.x = packet.x
            end.y = packet.y
            end.z = packet.z
            event.packet = end
        }
    }
}