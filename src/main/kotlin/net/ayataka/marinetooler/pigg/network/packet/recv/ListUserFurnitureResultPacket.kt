package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.area.StockFurniture

open class ListUserFurnitureResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LIST_USER_FURNITURE_RESULT.id

    var max = 0
    var furnitures: MutableList<StockFurniture> = mutableListOf()
    var placedFurnitures = mutableMapOf<String, String>()
    var roomData = mutableListOf<String>()
    var roomNum = 0

    override fun readFrom(buffer: ByteBuilder) {
        max = buffer.readInt()

        (0 until buffer.readInt()).forEach {
            furnitures.add(StockFurniture().apply {
                quantity = buffer.readInt()
                characterId = buffer.readString()
                type = buffer.readByte()
                category = buffer.readString()
                name = buffer.readString()
                description = buffer.readString()
                actionCode = buffer.readString()

                (0 until buffer.readShort()).forEach {
                    parts.add(PartData(false).apply { readFrom(buffer) })
                }

                time = buffer.readDouble()
            })
        }

        roomNum = buffer.readInt()

        (0 until roomNum).forEach {
            val roomName = buffer.readString()

            roomData.add(roomName)

            (0 until buffer.readInt()).forEach {
                placedFurnitures[buffer.readString()] = buffer.readString()
            }
        }
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(max, furnitures.size)

        furnitures.forEach {
            buffer.writeInt(it.quantity)
                    .writeString(it.characterId)
                    .writeByte(it.type)
                    .writeString(it.category, it.name, it.description, it.actionCode)
                    .writeShort(it.parts.size.toShort())

            it.parts.forEach {
                it.writeTo(buffer)
            }

            buffer.writeDouble(it.time)
        }

        buffer.writeInt(roomNum)

        roomData.forEach {
            buffer.writeString(it)
                    .writeInt(placedFurnitures.size)

            placedFurnitures.forEach {
                buffer.writeString(it.key, it.value)
            }
        }

        return buffer
    }
}