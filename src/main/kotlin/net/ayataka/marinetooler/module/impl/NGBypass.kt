package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
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
    }
}