package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.CheckBanWordResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.OneMessageSavePacket
import net.ayataka.marinetooler.pigg.network.packet.send.SystemActionPacket
import net.ayataka.marinetooler.pigg.network.packet.send.TalkPacket
import org.apache.commons.io.IOUtils

object NGBypass : Module() {
    private val ngWords = IOUtils.readLines(javaClass.classLoader.getResourceAsStream("ng-words.txt"))!!

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

        if (packet is ActionPacket) {
            if (packet.actionId == "sad") {
                packet.actionId = "ranaruta\u0000_secret"
                Pigg.send(SystemActionPacket().apply { actionCode = "ranaruta" })
            }
        }
    }

    @EventListener
    fun onReceive(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is CheckBanWordResultPacket) {
            packet.isBan = false
        }
    }
}