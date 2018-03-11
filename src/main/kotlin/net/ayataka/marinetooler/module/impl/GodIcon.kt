package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.ListUserItemResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket

object GodIcon : Module() {
    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is ListUserItemResultPacket) {
            if(String(packet.data).contains("初心者バッジ")){
                val pac = ByteBuilder()
                        .writeRawInt(1)
                        .writeRawByte(1)
                        .writeBoolean(true)

                        .build().array()

                packet.data.plus(pac)
            }
        }
    }
}