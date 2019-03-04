package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.CheckBanWordResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.OneMessageSavePacket
import net.ayataka.marinetooler.pigg.network.packet.send.TalkPacket

object NGBypass : Module() {
    private val ngWords = javaClass.classLoader.getResourceAsStream("ng-words.txt").reader().readLines()

    @EventListener
    fun onSend(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is TalkPacket) {
            for (line in ngWords) {
                packet.text = packet.text.replace(line, line.first() + "\n" + line.substring(1))
            }
        }

        if (packet is OneMessageSavePacket) {
            for (line in ngWords) {
                packet.text = packet.text.replace(line, line.first() + "\n" + line.substring(1))
            }

            packet.text = packet.text.replace("\\n", "\n")
        }
    }

    @EventListener
    fun onReceive(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is CheckBanWordResultPacket) {
            packet.isBan = false
        }
    }
}