package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult
import net.ayataka.marinetooler.pigg.network.packet.recv.GetUserProfileResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.RoomActionResult
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.RoomActionPacket
import net.ayataka.marinetooler.utils.info
import kotlin.concurrent.thread

object FakeEquipment : Module() {
    fun addEquipment(usercode: String, equipment: String){
        val avatarData = Pigg.userEquipments[usercode]?.apply { item.items.add(equipment) } ?: CurrentUser.areaData.defineAvatars
                .find { it.data.userCode == usercode }?.data!!
                .apply {
                    item.items.add(equipment)
                    Pigg.userEquipments[usercode] = this
                }

        val packet = FinishDressupResult().apply {
            this.avatarData = avatarData
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

    fun deleteEquipment(usercode: String, equipment: String){
        val avatarData = Pigg.userEquipments[usercode]?.apply { item.items.remove(equipment) }!!

        val packet = FinishDressupResult().apply {
            this.avatarData = avatarData
            this.usercode = usercode
        }

        Pigg.receive(packet)

        if(usercode != CurrentUser.usercode){
            return
        }

        val actionPacket = RoomActionPacket().apply {
            actionCode = "unequip:$equipment"
        }

        Pigg.send(actionPacket)
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent){
        val packet = event.packet

        if(packet is RoomActionResult){
            if(packet.userCode == CurrentUser.usercode){
                return
            }

            val command = packet.actionCode.split(":")

            when(command[0]){
                "equip" -> addEquipment(packet.userCode, command[1])
                "unequip" -> deleteEquipment(packet.userCode, command[1])
            }
        }
        else if(packet is BaseAreaData){
            packet.defineAvatars.filter { Pigg.userEquipments.containsKey(it.data.userCode) }.forEach {
                it.data = Pigg.userEquipments[it.data.userCode]!!
            }
        }
        else if(packet is GetUserProfileResultPacket){
            Pigg.userEquipments[packet.usercode]?.let {
                packet.defineAvatar = DefineAvatar().apply { load(it) }
            }
        }
    }
}