package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.area.StockFurniture
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet

open class ListUserFurnitureResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_USER_FURNITURE_RESULT.id

    var max = 0
    var furnitures: MutableList<StockFurniture> = mutableListOf()
    var placedFurnitures: MutableList<String> = mutableListOf()
    var roomNum = 0
    var loc2 = 0
    var loc5 = mutableListOf<Short>()
    var whatStrings1 = mutableListOf<String>()
    var whatStrings2 = mutableListOf<MutableList<String>>()
    var loc10 = mutableListOf<Int>()

    override fun readFrom(buffer: ByteBuilder) {
        max = buffer.readInt()

        loc2 = buffer.readInt()

        for (i in 1..loc2){
            val furniture = StockFurniture()

            furniture.quantity = buffer.readInt()
            furniture.characterId = buffer.readString()
            furniture.type = buffer.readByte()
            furniture.category = buffer.readString()
            furniture.name = buffer.readString()
            furniture.description = buffer.readString()
            furniture.actionCode = buffer.readString()

            val loc5 = buffer.readShort()

            //loc5[i - 1] = loc5
            loc5.add(loc5)

            val parts = mutableListOf<PartData>()

            for(i2 in 1..loc5){
                val part = PartData(false)
                part.readFrom(buffer)

                parts.add(part)
            }

            furniture.parts = parts
            furniture.time = buffer.readDouble()

            furnitures.add(furniture)
        }

        roomNum = buffer.readInt()

        for (i in 1..roomNum){
            val whatString1= buffer.readString()
            val loc10 = buffer.readInt()

            //whatStrings1[i - 1] = whatString1
            //loc10[i - 1] = loc10

            whatStrings1.add(whatString1)
            loc10.add(loc10)

            for(i2 in 1..loc10){
                val whatString2 = buffer.readString()

                whatStrings2.add(mutableListOf(whatString2))
                placedFurnitures.add(buffer.readString())
            }
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}