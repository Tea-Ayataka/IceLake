package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.info

object Command : Module() {
    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is TalkPacket) {
            val text = packet.text

            if (text.startsWith(".")) {
                event.packet = CancelTypingPacket()
                this.doCommand(text.substring(1))
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
                    NoticeSpammer.spam(spitted[1])
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