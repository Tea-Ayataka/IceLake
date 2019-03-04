package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.action.ActionData
import net.ayataka.marinetooler.pigg.network.packet.recv.ListActionResult
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.SystemActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.UpdateActionPacket
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.trace

object ActionModifier : Module() {
    private var defaultActions = listOf<ActionData>()

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is ListActionResult) {
            event.canceled = true
            defaultActions = packet.actions
            loadActions()
        }
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is ActionPacket) {
            packet.actionId = packet.actionId.replace("#", "\u0000")

            if ("ranaruta" in packet.actionId) {
                PiggProxy.send(SystemActionPacket().apply { actionCode = "ranaruta" })
            }
        }

        if (packet is UpdateActionPacket) {
            event.canceled = true

            packet.actions.forEach {
                ICE_LAKE.config.actionOrders[it.removeSuffix("#_secret")] = packet.actions.indexOf(it)
            }

            loadActions()

            ICE_LAKE.saveConfig()
        }
    }

    private fun loadActions() {
        val actions = defaultActions.toMutableList()
        actions.forEach { it.order = -1 }

        ICE_LAKE.actions
                .filter { actions.none { item -> it.key == item.code } }
                .toList()
                .sortedBy { it.second.ifEmpty { it.first } }
                .forEach {
                    actions.add(ActionData().apply {
                        code = it.first + "#_secret"
                        title = it.second.ifEmpty { it.first }
                        order = Int.MAX_VALUE
                        trace("Adding action: ${it.first}")
                    })
                }

        actions.forEach { action ->
            ICE_LAKE.config.actionOrders[action.code.removeSuffix("#_secret")]?.let {
                action.order = it
            }
        }

        PiggProxy.receive(ListActionResult().apply {
            this.actions = actions.sortedBy { it.order }
        })

        info("Loaded ${actions.size} actions")
    }
}