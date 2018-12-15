package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket
import net.ayataka.marinetooler.utils.math.Vec3i
import kotlin.concurrent.timer

object ClickTP : Module() {
    var meme = true

    var pos: Vec3i = CurrentUser.location.clone()
    var moving = false

    @Suppress("ImplicitThis")
    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is MovePacket) {
            if (meme) {
                zigzag(Vec3i(packet.x.toInt(), packet.y.toInt(), packet.z.toInt()))
            } else {
                // クリック式瞬間移動
                val end = MoveEndPacket()
                end.x = packet.x
                end.y = packet.y
                end.z = packet.z
                event.packet = end
            }
        }
    }

    fun zigzag(destination: Vec3i) {
        if (moving) {
            return
        }

        moving = true
        timer(period = 50) {
            pos.x += if (destination.x > pos.x) 1 else if (destination.x < pos.x) -1 else 0
            pos.y += if (destination.y > pos.y) 1 else if (destination.y < pos.y) -1 else 0
            pos.z = destination.z

            CurrentUser.teleport(pos.x, pos.y, pos.z, 0)

            if (pos == destination) {
                cancel()
                moving = false
            }
        }
    }
}