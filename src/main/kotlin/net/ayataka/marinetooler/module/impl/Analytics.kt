package net.ayataka.marinetooler.module.impl

import javafx.application.Platform
import net.ayataka.eventapi.EventListener
import net.ayataka.eventapi.EventPriority
import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.shop.ShopEcItemData
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.GetPiggShopCategoryResult
import net.ayataka.marinetooler.pigg.network.packet.recv.GetShopResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.ListActionResult
import net.ayataka.marinetooler.pigg.network.packet.send.ClickPiggShopItemPacket
import net.ayataka.marinetooler.utils.IceLakeApi

object Analytics : Module() {
    @EventListener(priority = EventPriority.HIGH)
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is ListActionResult) {
            Thread {
                packet.actions
                        .filter { it.code !in ICE_LAKE.actions.keys }
                        .forEach { IceLakeApi.registerAction(it.code, it.type, it.title) }
            }.start()
        }

        if (packet is ActionResultPacket) {
            val code = packet.actionCode.takeWhile { it != '\u0000' }

            if (code !in ICE_LAKE.actions.keys) {
                Thread {
                    IceLakeApi.registerAction(code, "", "")
                }.start()
            }
        }

        if (packet is GetPiggShopCategoryResult) {
            onReceiveItems(packet.items)
        }

        if (packet is GetShopResultPacket) {
            Thread {
                IceLakeApi.registerShop(packet.shop.shopCode, packet.shop.name)
            }.start()
        }
    }

    @EventListener(priority = EventPriority.HIGH)
    fun onSendPacket(event: SendPacketEvent) = Platform.runLater {
        val packet = event.packet

        if (packet is ClickPiggShopItemPacket && packet.itemType == "action") {
            val code = packet.itemCode.takeWhile { it != '\u0000' }

            if (code !in ICE_LAKE.actions.keys) {
                Thread {
                    IceLakeApi.registerAction(code, "", "")
                }.start()
            }
        }
    }

    private fun onReceiveItems(items: List<ShopEcItemData>) {
        Thread {
            items
                    .filter { it.type == "action" && it.itemId !in ICE_LAKE.actions.filter { it.value.isNotEmpty() }.keys }
                    .forEach {
                        Thread {
                            IceLakeApi.registerAction(it.itemId, "", it.name)
                        }.start()
                    }
        }.start()
    }
}