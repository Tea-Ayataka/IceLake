package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.info

object Command : Module() {
    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is TalkPacket) {
            val text = packet.text

            if (text.startsWith(".")) {
                event.packet = CancelTypingPacket()
                doCommand(text.substring(1))
            }
        }
    }

    fun doCommand(string: String) {
        try {
            val spitted = string.split(" ")

            when (spitted[0].toLowerCase()) {
                "act" -> {
                    CurrentUser.playAction(spitted[1])
                }
                "area" -> {
                    val packet = GetAreaPacket()
                    packet.category = spitted[1].split(".")[0]
                    packet.code = spitted[1].split(".")[1]
                    Pigg.send(packet)
                }
                "travel" -> {
                    val packet = TravelBundlePacket()
                    packet.categoryCode = spitted[1]
                    packet.areaCode = spitted[2]
                    Pigg.send(packet)
                }
                "shop" -> {
                    val packet = GetShopPacket()
                    packet.shopCode = spitted[1]
                    Pigg.send(packet)
                }
                "addfish" -> {
                    AquariumSpammer.addFish(spitted[1])
                }
                "spam" -> {
                    NoticeSpammer.enabled = !NoticeSpammer.enabled
                }
                "m" -> {
                    FurnitureExploiter.move(-1, spitted[1].toInt(), spitted[2].toInt(), spitted[3].toInt(), spitted[4].toByte())
                }
                "place" -> {
                    FurnitureExploiter.place(spitted[1], spitted[2].toInt(), spitted[3].toInt(), spitted[4].toInt(), spitted[5].toByte())
                }
                "del" -> {
                    FurnitureExploiter.remove(spitted[1].toInt())
                }
                "roomact" -> {
                    val packet = RoomActionPacket()
                    packet.actionCode = spitted[1]

                    Pigg.send(packet)
                }
                else -> {
                    info("無効なコマンドです")
                }
            }
        } catch (ex: Exception) {
            info("エラー: ${ex.javaClass.name}")
        }
    }
}