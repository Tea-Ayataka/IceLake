package net.ayataka.marinetooler.module.impl

import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult

object FakeEquipment : Module() {
    val equipments = mutableListOf<String>()

    fun addEquipment(equipment: String){
        equipments.add(equipment)

        val packet = FinishDressupResult().apply {
            avatarData = CurrentUser.avatarData.apply { item.items.add(equipment) }
            usercode = CurrentUser.usercode!!
        }

        Pigg.receive(packet)
    }
}