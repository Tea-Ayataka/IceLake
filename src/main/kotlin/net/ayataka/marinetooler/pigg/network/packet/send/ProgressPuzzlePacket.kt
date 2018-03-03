package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class ProgressPuzzlePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PROGRESS_PUZZLE.id
    override val encrypted = true

    var actionId = ""

    override fun readFrom(buffer: ByteBuilder) {
        info(" ${String(buffer.readAllBytes())}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}