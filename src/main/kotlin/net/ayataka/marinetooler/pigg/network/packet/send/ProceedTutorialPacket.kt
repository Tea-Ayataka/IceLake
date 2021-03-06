package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info

class ProceedTutorialPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PROCEED_TUTORIAL.id

    var step = 0

    override fun readFrom(buffer: ByteBuilder) {
        step = buffer.readInt()

        info(" STEP ID IS ${step}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(step)
        return buffer
    }
}