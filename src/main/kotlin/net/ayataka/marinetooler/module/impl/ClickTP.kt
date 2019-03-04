package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket
import net.ayataka.marinetooler.pigg.network.packet.send.SystemActionPacket
import net.ayataka.marinetooler.utils.math.Vec3i
import java.util.*
import kotlin.concurrent.timer

object ClickTP : Module() {
    var meme = false

    var pos: Vec3i = CurrentUser.location.clone()
    var moving = false

    // Rotate
    var direction = -1
    var rotater: Timer? = null
    var fucked: Int = 0
    var fuckeing = false

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
                event.canceled = true
                PiggProxy.send(end)
                // Pigg.send(SystemActionPacket().apply { actionCode = "stop" })
            }
        }
    }

    fun zigzag(destination: Vec3i) {
        if (moving) {
            return
        }

        moving = true
        direction = 0
        timer(period = 50) {
            var addedX = false

            if (direction % 2 == 0) {
                pos.x += if (destination.x > pos.x) {
                    addedX = true
                    1
                } else if (destination.x < pos.x) {
                    addedX = true
                    -1
                } else {
                    0
                }

                if (!addedX) {
                    pos.y += if (destination.y > pos.y) 1 else if (destination.y < pos.y) -1 else 0
                }
            } else {
                pos.y += if (destination.y > pos.y) {
                    addedX = true; 1
                } else if (destination.y < pos.y) {
                    addedX = true;-1
                } else 0

                if (!addedX) {
                    pos.x += if (destination.x > pos.x) 1 else if (destination.x < pos.x) -1 else 0
                }
            }

            pos.z = destination.z

            CurrentUser.teleport(pos.x, pos.y, pos.z, direction.toByte())

            direction++
            if (direction >= 4) {
                direction = 0
            }

            if (pos == destination) {
                cancel()
                moving = false
                PiggProxy.send(SystemActionPacket().apply { actionCode = "stop" })
            }
        }
    }

    fun rotateStart() {
        if (rotater != null) {
            return
        }

        direction = 0
        rotater = timer(period = 50) {
            CurrentUser.teleport(CurrentUser.location.x, CurrentUser.location.y, CurrentUser.location.z, direction.toByte())

            if (fuckeing) {
                direction++
            } else {
                direction--
            }

            if ((fuckeing && direction >= 4) || (!fuckeing && direction <= 0)) {
                direction = if (fuckeing) 0 else 3
            }

            fucked++

            if (fucked > 50) {
                fucked = 0
                fuckeing = !fuckeing
            }
        }
    }

    fun rotateStop() {
        rotater?.cancel()
        rotater = null
    }
}