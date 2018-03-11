package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.send.ChangeWindowAquariumPacket
import net.ayataka.marinetooler.pigg.network.packet.send.TableGamePacket

object AutoBJ : Module() {
    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet
    }
}