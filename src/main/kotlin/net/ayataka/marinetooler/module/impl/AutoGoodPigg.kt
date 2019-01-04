package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.GetUserProfilePacket
import net.ayataka.marinetooler.pigg.network.packet.send.GoodPiggPacket
import net.ayataka.marinetooler.utils.info

object AutoGoodPigg : Module() {
    val goodpigged = mutableListOf<String>()

    @Volatile
    var running = false

    override fun onEnable() {
        start()
    }

    private fun start() {
        running = true

        Thread {
            val target = CurrentUser.areaData.defineAvatars.filter { it.data.userCode != CurrentUser.usercode }.map { it.data.userCode }.toMutableList()

            if (target.isEmpty()) {
                running = false
                return@Thread
            }

            target.removeAll(goodpigged)

            target.forEach {
                sendGoodPigg(it)
                goodpigged.add(it)
                Thread.sleep(300)
            }

            running = false
        }.start()
    }

    private fun sendGoodPigg(userCode: String) {
        info("GOOD PIGG $userCode")
        val profile = GoodPiggPacket()
        profile.usercode = userCode
        Pigg.send(profile)
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet
        if (running) {
            if (packet is GetUserProfileResultPacket) {
                packet.canceled = true
            }
            if (packet is ErrorPacket) {
                packet.canceled = true
            }
        }

        if (!running && (packet is EnterUserRoomResult || packet is EnterUserGardenResult || packet is EnterAreaResult)) {
            start()
        }

        if (!running && packet is AppearUserPacket && !goodpigged.contains(packet.usercode)) {
            val user = packet.usercode

            Thread {
                running = true
                sendGoodPigg(user)
                goodpigged.add(user)
                Thread.sleep(300)
                running = false
            }.start()
        }
    }

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet
        if (packet is GetUserProfilePacket) {

        }
    }
}