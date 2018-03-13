package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.readUCodesFromAreaPacket

open class BaseAreaData : Packet() {
    override val server = ServerType.CHAT
    override val packetId = InfoPacketID.NONE.id

    var users = mutableListOf<String>()

    lateinit var areaData: AreaData

    override fun readFrom(buffer: ByteBuilder) {
        this.areaData = AreaData()

        this.areaData.readFrom(buffer)

        // Get area users
        this.users = readUCodesFromAreaPacket(buffer)

        val placeFurnitures = mutableListOf<PlaceFurniture>()

        for(i in 1..buffer.readInt()){
            val placeFurniture = PlaceFurniture()

            placeFurniture.characterId = buffer.readString()
            placeFurniture.sequence = buffer.readInt()

            placeFurniture.x = buffer.readShort()
            placeFurniture.y = buffer.readShort()
            placeFurniture.z = buffer.readShort()

            placeFurniture.direction = buffer.readByte()
            placeFurniture.ownerId = buffer.readString()

            placeFurnitures.add(placeFurniture)
        }

        val defineFurnitures = mutableListOf<DefineFurniture>()

        for(i in 1..buffer.readInt()){
            val loc13 = buffer.readShort()
            val defineFurniture = DefineFurniture()

            defineFurniture.characterId = buffer.readString()
            defineFurniture.type = buffer.readByte()
            defineFurniture.category = buffer.readString()
            defineFurniture.name = buffer.readString()
            defineFurniture.description = buffer.readString()
            defineFurniture.actionCode = buffer.readString()

            for (i2 in 1..loc13){
                val partData = PartData(true)

                partData.readFrom(buffer)

                defineFurniture.parts.add(partData)
            }

            defineFurnitures.add(defineFurniture)
        }


    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}