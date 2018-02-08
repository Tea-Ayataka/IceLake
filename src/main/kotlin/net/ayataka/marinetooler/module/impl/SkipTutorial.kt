package net.ayataka.marinetooler.module.impl

import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.recv.ErrorPacket
import net.ayataka.marinetooler.pigg.network.packet.send.ProceedTutorialPacket

object SkipTutorial : Module() {
    fun skip() {
        val packet = ProceedTutorialPacket()
        packet.step = 13
        Pigg.send(packet)

        val error = ErrorPacket()
        error.code = "common.unknown"
        error.message = "kiritodaisuki"
        Pigg.receive(error)
    }
}