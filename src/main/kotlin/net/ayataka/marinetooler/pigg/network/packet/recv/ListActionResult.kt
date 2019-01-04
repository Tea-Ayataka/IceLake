package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.action.ActionData
import net.ayataka.marinetooler.utils.compress
import net.ayataka.marinetooler.utils.decompress
import net.ayataka.marinetooler.utils.dump

class ListActionResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_ACTION_RESULT.id

    var actions: List<ActionData> = listOf()

    override fun readFrom(buffer: ByteBuilder) {
        val size = buffer.readInt()
        buffer.skip(4) // skip unused data

        @Suppress("NAME_SHADOWING")
        val buffer = buffer.readAllBytes().decompress()

        actions = (0 until size).map {
            ActionData().apply { readFrom(buffer) }
        }

        dump("Actions (${actions.size}):")
        actions.forEach {
            dump(it.toString())
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(actions.size)
        buffer.skip(2)

        val toCompress = ByteBuilder()
        actions.forEach {
            it.writeTo(toCompress)
        }

        buffer.writeBytes(toCompress.compress())
        return buffer
    }
}