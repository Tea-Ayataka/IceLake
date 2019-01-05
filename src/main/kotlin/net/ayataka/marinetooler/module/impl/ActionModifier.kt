package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.action.ActionData
import net.ayataka.marinetooler.pigg.network.packet.recv.ListActionResult
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.SystemActionPacket

object ActionModifier : Module() {
    private val CUSTOM_ACTIONS = listOf(
            "maximum_secret" to "デカマム",
            "bigface_secret" to "ビッグフェイス",
            "ranaruta" to "メイド・イン・ヘブン",
            "hello#equip patrol_symbol_suit patrol_symbol_word" to "ピグパトロールに変身！"
    )

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is ListActionResult) {
            val actions = packet.actions.toMutableList()

            var index = actions.size
            CUSTOM_ACTIONS.forEach {
                index++
                actions.add(ActionData().apply {
                    code = it.first + "#_secret"
                    title = it.second
                    order = index
                })
            }

            packet.actions = actions
        }
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is ActionPacket) {
            packet.actionId = packet.actionId.replace("#", "\u0000")

            if ("ranaruta" in packet.actionId) {
                Pigg.send(SystemActionPacket().apply { actionCode = "ranaruta" })
            }
        }
    }
}