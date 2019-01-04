package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult
import net.ayataka.marinetooler.pigg.network.packet.recv.TalkResultPacket
import java.awt.Color

object FakeEquipment : Module() {
    val equipments = mutableListOf<String>()

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is BaseAreaData) {
            packet.defineAvatars.find { it.data.userCode == CurrentUser.usercode }?.data?.item?.items?.addAll(equipments)
        }
    }

    fun addEquipment(equipment: String){
        equipments.add(equipment)

        val packet = FinishDressupResult().apply {
            avatarData = CurrentUser.avatarData.apply { item.items.add(equipment) }
            usercode = CurrentUser.usercode!!
        }

        Pigg.receive(packet)
    }
}