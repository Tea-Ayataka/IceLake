package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket
import net.ayataka.marinetooler.pigg.network.packet.send.SystemActionPacket
import net.ayataka.marinetooler.utils.info
import java.util.*
import kotlin.concurrent.timer

object Skeet : Module() {
    private var skeeting = false
    var task: Timer? = null

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is MovePacket) {
            this.skeeting = true
            this.task?.cancel()
            this.task?.purge()
            this.task = timer(period = 50) {
                this@Skeet.sendStop()
            }
        }

        if (packet is MoveEndPacket) {
            info("STOPPED SKEET")
            this.skeeting = false
            this.task?.cancel()
            this.task?.purge()
        }
    }

    private fun sendStop() {
        val canceler = SystemActionPacket()
        canceler.actionCode = "stop"
        Pigg.send(canceler)
    }

    override fun onDisable() {
        this.task?.cancel()
        this.task?.purge()
    }
}