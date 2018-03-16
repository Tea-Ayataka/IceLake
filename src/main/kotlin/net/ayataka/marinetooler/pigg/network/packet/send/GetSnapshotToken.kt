package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

class GetSnapshotToken : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_SNAPSHOT_TOKEN.id
    override val encrypted = true

    var type = ""

    override fun readFrom(buffer: ByteBuilder) {
        this.type = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.type)
        return buffer
    }
}