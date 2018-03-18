package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ErrorPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.GetPiggShopCategory
import net.ayataka.marinetooler.pigg.network.packet.recv.GetPiggShopGachaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.PlayGachaStepupPacket
import net.ayataka.marinetooler.utils.info
import kotlin.concurrent.timer

object MuryouGatyaZenkaihou : Module() {
    private val queue = mutableListOf<String>()

    override fun onEnable() {
        queue.clear()
        val packet = GetPiggShopCategory()
        packet.type = "gacha"
        packet.category = "all"

        Pigg.send(packet)
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is ErrorPacket) {
            packet.canceled = true
        }

        if (packet is GetPiggShopGachaResultPacket) {
            info("Category ${packet.category}")
            info("Type ${packet.type}")
            info("Items ${packet.items.filter { it.remainingFreePlayCount > 0 }.joinToString(transform = { it.code + ":" + it.freePlayCountLabelState })}")
            info("Amount ${packet.items.filter { it.remainingFreePlayCount < 1 }.size}")
            info("Amount ${packet.items.filter { it.remainingFreePlayCount > 0 }.size}")

            queue.addAll(packet.items.filter { it.remainingFreePlayCount > 0 && it.freePlayCountLabelState == 1.toByte()}.map { it.code })

            timer(period = 1100) {
                if (queue.isEmpty()) {
                    cancel()
                    info("Completed Gacha")
                    CurrentUser.showAlert("Completed Gacha")

                    return@timer
                }

                doGacha(queue.first())
                queue.removeAt(0)
            }
        }
    }

    private fun doGacha(code: String) {
        val packet = PlayGachaStepupPacket()
        packet.gachaCode = code
        packet.categoryType = 58
        packet.useCoupon = false
        packet.useFreeAccount = true
        packet.playType = 0
        packet.spendType = 0

        Pigg.send(packet)
        info("Play! $code")
    }
}