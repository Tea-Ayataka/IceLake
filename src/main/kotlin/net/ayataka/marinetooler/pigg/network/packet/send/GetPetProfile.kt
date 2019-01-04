package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

class GetPetProfile : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_PET_PROFILE.id

    var petId = 0
    var userCode = ""

    override fun readFrom(buffer: ByteBuilder) {
        userCode = buffer.readString()
        petId = buffer.readInt()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(userCode)
        buffer.writeInt(petId)

        return buffer
    }
}