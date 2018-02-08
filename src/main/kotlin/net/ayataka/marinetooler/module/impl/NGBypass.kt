package net.ayataka.marinetooler.module.impl

import com.darkmagician6.eventapi.EventTarget
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.TalkPacket
import org.apache.commons.io.IOUtils

object NGBypass : Module() {
    private val ngWords = IOUtils.readLines(javaClass.classLoader.getResourceAsStream("ng-words.txt"))!!

    @EventTarget
    fun onSend(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is TalkPacket) {
            for (line in this.ngWords) {
                packet.text = packet.text.replace(line, line.first() + "\n" + line.substring(1))
            }
        }
    }
}