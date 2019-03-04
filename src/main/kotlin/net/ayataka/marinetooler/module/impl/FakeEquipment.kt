package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.FinishDressupResult
import net.ayataka.marinetooler.pigg.network.packet.recv.GetUserProfileResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket

object FakeEquipment : Module() {
    private val customEquipments: MutableList<String> = mutableListOf()

    fun addEquipment(usercode: String, vararg equipment: String) {
        val avatarData = getAvatarData(usercode)?.apply { item.items.addAll(equipment) } ?: return

        PiggProxy.receive(FinishDressupResult().apply {
            this.avatarData = avatarData
            this.usercode = usercode
        })

        if (usercode == CurrentUser.usercode) {
            PiggProxy.send(ActionPacket().apply {
                actionId = "hello\u0000equip ${equipment.joinToString(" ")}"
            })
        }
    }

    fun deleteEquipment(usercode: String, vararg equipment: String) {
        val avatarData = getAvatarData(usercode)?.apply { item.items.removeAll(equipment) } ?: return

        PiggProxy.receive(FinishDressupResult().apply {
            this.avatarData = avatarData
            this.usercode = usercode
        })

        if (usercode == CurrentUser.usercode) {
            PiggProxy.send(ActionPacket().apply {
                actionId = "hello\u0000unequip ${equipment.joinToString(" ")}"
            })
        }
    }

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is ActionResultPacket) {
            if (!packet.actionCode.startsWith("hello\u0000")) {
                return
            }

            event.canceled = true

            val data = packet.actionCode.split("\u0000")[1].split(" ")
            when (data[0]) {
                "equip" -> addEquipment(packet.usercode, *data.drop(1).toTypedArray())
                "unequip" -> deleteEquipment(packet.usercode, *data.drop(1).toTypedArray())
            }
        }

        //BaseAreaDataが送られてきた直後に送っても反映されないからBaseAreaDataのパケットで変えるかスリープを付けたほうがいい
        if (packet is BaseAreaData) {
            val usercode = CurrentUser.usercode ?: return
            addEquipment(usercode, *customEquipments.toTypedArray())
        }

        if (packet is GetUserProfileResultPacket) {
            /*getAvatarData(packet.usercode)?.let {
                packet.defineAvatar = DefineAvatar().apply { load(it) }
            }*/
        }
    }

    private fun getAvatarData(usercode: String): AvatarData? {
        return CurrentUser.areaData.defineAvatars.find { it.data.userCode == usercode }?.data
    }
}