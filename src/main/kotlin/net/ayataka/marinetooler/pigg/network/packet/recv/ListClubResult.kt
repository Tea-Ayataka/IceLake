package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.club.ClubAreaData

class ListClubResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_CLUB_RESULT.id

    var userId = 0
    var loc2 = ""
    var loc3 = ""
    var loc4 = ""
    var clubAreaDatas = mutableListOf<ClubAreaData>()
    var loc7 = false

    override fun readFrom(buffer: ByteBuilder) {
        userId = buffer.readInt()
        loc2 = buffer.readString()
        loc3 = buffer.readString()
        loc4 = buffer.readString()

        (0 until buffer.readInt()).forEach {
            clubAreaDatas.add(ClubAreaData().apply { readFrom(buffer) })
        }

        loc7 = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(userId)
                .writeString(loc2, loc3, loc4)
                .writeInt(clubAreaDatas.size)

        clubAreaDatas.forEach {
            it.writeTo(buffer)
        }

        buffer.writeBoolean(loc7)

        return buffer
    }
}