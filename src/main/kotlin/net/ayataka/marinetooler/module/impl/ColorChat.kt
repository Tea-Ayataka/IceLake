package net.ayataka.marinetooler.module.impl

import javafx.scene.paint.Color
import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.TalkPacket

object ColorChat : Module() {
    var color = Color.WHITE!!
    var hue = 0

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is TalkPacket) {
            packet.color = getRGB(Color.hsb(hue.toDouble(), 0.5, 1.0))
            hue += 50
            if (hue > 360) {
                hue = 0
            }
        }
    }

    private fun getRGB(color: Color): Int {
        val r = (color.red * 0xFF).toInt()
        val g = (color.green * 0xFF).toInt()
        val b = (color.blue * 0xFF).toInt()
        val a = ((1.0 - color.opacity) * 0xFF).toInt()

        return a.shl(24) + r.shl(16) + g.shl(8) + b
    }
}