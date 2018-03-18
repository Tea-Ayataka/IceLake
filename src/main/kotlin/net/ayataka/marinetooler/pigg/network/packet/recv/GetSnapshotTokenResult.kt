package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetSnapshotTokenResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_SNAPSHOT_TOKEN_RESULT.id

    var token = ""
    var type = ""

    override fun readFrom(buffer: ByteBuilder) {
        token = buffer.readString()
        type = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}