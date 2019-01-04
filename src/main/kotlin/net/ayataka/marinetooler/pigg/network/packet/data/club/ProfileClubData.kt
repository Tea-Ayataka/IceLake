package net.ayataka.marinetooler.pigg.network.packet.data.club

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class ProfileClubData : PacketData {
    var clubId = ""
    var clubName = ""
    var emblemData = ClubEmblemData()

    override fun readFrom(buffer: ByteBuilder) {
        clubId = buffer.readString()
        clubName = buffer.readString()
        emblemData = ClubEmblemData().apply { readFrom(buffer) }
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(clubId, clubName)

        emblemData.writeTo(buffer)
    }
}