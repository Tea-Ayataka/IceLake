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
        this.max = buffer.readInt()

        this.loc2 = buffer.readInt()

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

            //this.loc5[i - 1] = loc5
            this.loc5.add(loc5)

            val parts = mutableListOf<PartData>()

            for(i2 in 1..loc5){
                val part = PartData(false)
                part.readFrom(buffer)

                parts.add(part)
            }

            furniture.parts = parts
            furniture.time = buffer.readDouble()

            this.furnitures.add(furniture)
        }

        this.roomNum = buffer.readInt()

        for (i in 1..this.roomNum){
            val whatString1= buffer.readString()
            val loc10 = buffer.readInt()

            //this.whatStrings1[i - 1] = whatString1
            //this.loc10[i - 1] = loc10

            this.whatStrings1.add(whatString1)
            this.loc10.add(loc10)

            for(i2 in 1..loc10){
                val whatString2 = buffer.readString()

                //this.whatStrings2[i - 1][i2 - 1] = whatString2
                this.whatStrings2.add(mutableListOf(whatString2))
                this.placedFurnitures.add(buffer.readString())
            }
        }
    }

    //TODO: writeToを完成させる
    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}