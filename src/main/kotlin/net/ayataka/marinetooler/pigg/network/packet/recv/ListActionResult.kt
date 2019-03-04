package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.action.ActionData
import net.ayataka.marinetooler.utils.compress
import net.ayataka.marinetooler.utils.decompress
import net.ayataka.marinetooler.utils.trace

class ListActionResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_ACTION_RESULT.id

    var actions: List<ActionData> = listOf()

    override fun readFrom(buffer: ByteBuilder) {
        val count = buffer.readInt()
        val length = buffer.readInt()

        val decompressed = buffer.readBytes(length).decompress()
        actions = (0 until count).map {
            ActionData().apply { readFrom(decompressed) }
        }

        trace("Actions (${actions.size}):")
        actions.forEach {
            trace(it.toString())
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(actions.size)

        val compressed = ByteBuilder().apply { actions.forEach { it.writeTo(this) } }.compress()
        buffer.writeInt(compressed.size)
        buffer.writeRawBytes(compressed)

        return buffer
    }
}