package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult
import net.ayataka.marinetooler.pigg.network.packet.recv.GetUserProfileResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket

object FakeEquipment : Module() {
    private val userEquipments = mutableMapOf<String, AvatarData>()

    fun addEquipment(usercode: String, equipment: String) {
        val avatarData = userEquipments[usercode]?.apply { item.items.add(equipment) }
                ?: CurrentUser.areaData.defineAvatars
                        .find { it.data.userCode == usercode }?.data!!
                        .apply {
                            item.items.add(equipment)
                            userEquipments[usercode] = this
                        }

        Pigg.receive(FinishDressupResult().apply {
            this.avatarData = avatarData
            this.usercode = usercode
        })

        if (usercode != CurrentUser.usercode) {
            return
        }

        Pigg.send(ActionPacket().apply {
            actionId = "hello\u0000equip#$equipment"
        })
    }

    fun deleteEquipment(usercode: String, equipment: String) {
        val avatarData = userEquipments[usercode]?.apply {
            item.items.remove(equipment)
        } ?: return

        Pigg.receive(FinishDressupResult().apply {
            this.avatarData = avatarData
            this.usercode = usercode
        })

        if (usercode != CurrentUser.usercode) {
            return
        }

        Pigg.send(ActionPacket().apply {
            actionId = "hello\u0000unequip#$equipment"
        })
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is ActionResultPacket) {
            if (packet.usercode == CurrentUser.usercode || !packet.actionCode.startsWith("hello\u0000")) {
                return
            }

            packet.canceled = true

            val data = packet.actionCode.split("\u0000")[1].split("#")
            when (data[0]) {
                "equip" -> addEquipment(packet.usercode, data[1])
                "unequip" -> deleteEquipment(packet.usercode, data[1])
            }
        }

        if (packet is BaseAreaData) {
            packet.defineAvatars
                    .filter { userEquipments.containsKey(it.data.userCode) }
                    .forEach { it.data = userEquipments[it.data.userCode]!! }
        }

        if (packet is GetUserProfileResultPacket) {
            userEquipments[packet.usercode]?.let {
                packet.defineAvatar = DefineAvatar().apply { load(it) }
            }
        }
    }
}