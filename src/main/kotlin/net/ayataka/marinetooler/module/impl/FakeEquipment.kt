package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket

object FakeEquipment : Module() {
    val equipments = mutableListOf<String>()

    fun addEquipment(usercode: String, equipment: String){
        equipments.add(equipment)

        val packet = FinishDressupResult().apply {
            avatarData = CurrentUser.avatarData.apply { item.items.add(equipment) }
            this.usercode = usercode
        }

        Pigg.receive(packet)

        if(usercode != CurrentUser.usercode){
            return
        }

        val actionPacket = ActionPacket().apply {
            actionId = "hello\u0000equip:$equipment"
        }

        Pigg.send(actionPacket)
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent){
        val packet = event.packet

        if(packet is ActionResultPacket){
            if(!packet.actionCode.contains("hello\u0000equip:")){
                return
            }

            val equipment = packet.actionCode.replace("hello\u0000equip:", "")

            addEquipment(packet.usercode, equipment)
        }
    }
}