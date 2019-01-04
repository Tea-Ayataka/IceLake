package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

class SetPetProfile : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.SET_PET_PROFILE.id

    var petId = 0
    var name = ""
    var description = ""
    var follow = true

    override fun readFrom(buffer: ByteBuilder) {
        petId = buffer.readInt()
        name = buffer.readString()
        description = buffer.readString()
        follow = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(petId)
        buffer.writeString(name)
        buffer.writeString(description)
        buffer.writeBoolean(follow)

        return buffer
    }
}