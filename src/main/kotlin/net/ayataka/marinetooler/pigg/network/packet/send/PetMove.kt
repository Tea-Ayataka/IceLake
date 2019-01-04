package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

class PetMove : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.PET_MOVE.id

    var petId = 0
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0

    override fun readFrom(buffer: ByteBuilder) {
        petId = buffer.readInt()

        x = buffer.readShort()
        y = buffer.readShort()
        z = buffer.readShort()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(petId)

        buffer.writeShort(x)
        buffer.writeShort(y)
        buffer.writeShort(z)

        return buffer
    }
}