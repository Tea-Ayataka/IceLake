package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.TableGamePacket
import java.util.*
import kotlin.concurrent.timer

object SlotMacro : Module() {
    private var slotTimer: Timer? = null

    override fun onEnable() {
        start()
    }

    override fun onDisable() {
        stop()
    }

    fun start() {
        stop()
        slotTimer = timer(period = 3000) {
            val endGame = TableGamePacket()
            endGame.method = "onEndGame"
            endGame.data = ByteArray(0)
            Pigg.send(endGame)
            CurrentUser.spinSlot()
        }
    }

    fun stop() {
        slotTimer?.cancel()
        slotTimer?.purge()
    }

    @EventListener
    fun onSend(event: SendPacketEvent) {
        if(event.packet is TableGamePacket){
            event.packet.canceled = true
        }
    }
}