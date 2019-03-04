package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.compress
import net.ayataka.marinetooler.utils.decompress
import net.ayataka.marinetooler.utils.info

class UpdateActionPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.UPDATE_ACTION.id

    var actions: List<String> = listOf()

    override fun readFrom(buffer: ByteBuilder) {
        val count = buffer.readInt()
        val decompressed = buffer.readAllBytes().decompress()

        actions = (0 until count).map { decompressed.readString() }

        info("Actions (${actions.size}):")
        actions.forEach {
            info(it)
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(actions.size)

        val compressed = ByteBuilder().apply { actions.forEach { writeString(it) } }.compress()
        buffer.writeRawBytes(compressed)

        return buffer
    }
}