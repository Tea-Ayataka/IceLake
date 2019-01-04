package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult
import net.ayataka.marinetooler.pigg.network.packet.recv.RoomActionResult
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.RoomActionPacket
import net.ayataka.marinetooler.utils.info

object FakeEquipment : Module() {
    val equipments = mutableListOf<String>()

    fun addEquipment(usercode: String, equipment: String){
        equipments.add(equipment)

        val packet = FinishDressupResult().apply {
            avatarData = CurrentUser.areaData.defineAvatars.find { it.data.userCode == usercode }?.data?.apply { item.items.add(equipment) }
            this.usercode = usercode
        }

        Pigg.receive(packet)

        if(usercode != CurrentUser.usercode){
            return
        }

        val actionPacket = RoomActionPacket().apply {
            actionCode = "equip:$equipment"
        }

        Pigg.send(actionPacket)
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent){
        val packet = event.packet

        if(packet is RoomActionResult){
            if(!packet.actionCode.contains("equip:") || packet.userCode == CurrentUser.usercode){
                return
            }

            val equipment = packet.actionCode.split("equip:")[1]

            addEquipment(packet.userCode, equipment)
        }
    }
}