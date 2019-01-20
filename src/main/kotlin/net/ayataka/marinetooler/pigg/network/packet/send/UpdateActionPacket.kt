package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.compress
import net.ayataka.marinetooler.utils.decompress
import net.ayataka.marinetooler.utils.trace

class UpdateActionPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.UPDATE_ACTION.id

    var actions: List<String> = listOf()

    override fun readFrom(buffer: ByteBuilder) {
        val size = buffer.readInt()

        @Suppress("NAME_SHADOWING")
        val buffer = buffer.readAllBytes().decompress()

        actions = (0 until size).map {
            buffer.readString()
        }

        trace("Actions (${actions.size}):")
        actions.forEach {
            trace(it.toString())
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(actions.size)

        val toCompress = ByteBuilder()
        actions.forEach {
            buffer.writeString(it)
        }

        buffer.writeBytes(toCompress.compress())
        return buffer
    }
}