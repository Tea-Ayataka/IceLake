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
    var goal: Vec3i = Vec3i()
    var moving = false

    @Suppress("ImplicitThis")
    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is MovePacket) {
            if (this.meme && !this.moving) {
                this.moving = true
                this.goal = Vec3i(packet.x.toInt(), packet.y.toInt(), packet.z.toInt())

                val task = timer(period = 50) {
                    pos.x += if (goal.x > pos.x) 1 else if (goal.x < pos.x) -1 else 0
                    pos.y = goal.y
                    pos.z += if (goal.z > pos.z) 1 else if (goal.z < pos.z) -1 else 0

                    CurrentUser.teleport(pos.x, pos.y, pos.z, 0)

                    if (pos.equals(goal)) {
                        this.cancel()
                        moving = false
                    }
                }

                timer(period = Long.MAX_VALUE, initialDelay = 5000) {
                    this.cancel()
                    task.purge()
                    moving = false
                }
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
}