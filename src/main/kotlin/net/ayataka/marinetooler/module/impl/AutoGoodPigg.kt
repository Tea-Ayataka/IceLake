package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.GetUserProfilePacket
import net.ayataka.marinetooler.pigg.network.packet.send.GoodPiggPacket
import net.ayataka.marinetooler.utils.info

object AutoGoodPigg : Module() {
    val goodpigged = mutableListOf<String>()

    var running = false

    private fun start() {
        this.running = true

        Thread({
            if (CurrentUser.areaData.users.isEmpty()) {
                this.running = false
                return@Thread
            }

            val target = CurrentUser.areaData.users
            target.removeAll(this.goodpigged)

            target.forEach {
                this.openProfile(it)
                Thread.sleep(300)
                this.sendGoodPigg(it)
                this.goodpigged.add(it)
            }

            Thread.sleep(300)
            this.running = false
        }).start()
    }

    private fun openProfile(userCode: String) {
        info("OPEN PROFILE $userCode")
        val profile = GetUserProfilePacket()
        profile.usercode = userCode
        Pigg.send(profile)
    }

    private fun sendGoodPigg(userCode: String) {
        info("GOOD PIGG $userCode")
        val profile = GoodPiggPacket()
        profile.usercode = userCode
        Pigg.send(profile)
    }

    @EventTarget
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet
        if (this.running) {
            if (packet is GetUserProfileResultPacket) {
                packet.canceled = true
            }
            if (packet is ErrorPacket) {
                packet.canceled = true
            }
        }

        if (!this.running && (packet is EnterUserRoomResult || packet is EnterUserGardenResult || packet is EnterAreaResult)) {
            this.start()
        }

        if (!this.running && packet is AppearUserPacket && !this.goodpigged.contains(packet.usercode)) {
            val user = packet.usercode

            Thread({
                this.running = true
                this.openProfile(user)
                Thread.sleep(300)
                this.sendGoodPigg(user)
                this.goodpigged.add(user)
                Thread.sleep(300)
                this.running = false
            }).start()
        }
    }

    @EventTarget
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is GetUserProfilePacket) {

        }
    }
}