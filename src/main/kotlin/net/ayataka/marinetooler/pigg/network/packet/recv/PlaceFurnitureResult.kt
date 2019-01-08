package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import net.ayataka.marinetooler.utils.dump

class PlaceFurnitureResult : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.PLACE_FURNITURE_RESULT.id

    var placeFurniture = PlaceFurniture()
    var defineFurniture = DefineFurniture()
    var partLength: Short = 0

    override fun readFrom(buffer: ByteBuilder) {
        placeFurniture.sequence = buffer.readInt()

        placeFurniture.x = buffer.readInt().toShort()
        placeFurniture.y = buffer.readInt().toShort()
        placeFurniture.z = buffer.readInt().toShort()

        placeFurniture.direction = buffer.readByte()
        placeFurniture.ownerId = buffer.readString()

        partLength = buffer.readShort()

        defineFurniture.characterId = buffer.readString()
        defineFurniture.type = buffer.readByte()
        defineFurniture.category = buffer.readString()
        defineFurniture.name = buffer.readString()
        defineFurniture.description = buffer.readString()
        defineFurniture.actionCode = buffer.readString()

        (0 until partLength).forEach {
            val partData = PartData(true)

            partData.readFrom(buffer)

            defineFurniture.parts.add(partData)
        }

        dump(toString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeInt(placeFurniture.sequence)

        buffer.writeInt(placeFurniture.x.toInt())
        buffer.writeInt(placeFurniture.y.toInt())
        buffer.writeInt(placeFurniture.z.toInt())
        buffer.writeByte(placeFurniture.direction)
        buffer.writeString(placeFurniture.ownerId)

        buffer.writeShort(defineFurniture.parts.size.toShort())

        buffer.writeString(defineFurniture.characterId)
        buffer.writeByte(defineFurniture.type)
        buffer.writeString(defineFurniture.category)
        buffer.writeString(defineFurniture.name)
        buffer.writeString(defineFurniture.description)
        buffer.writeString(defineFurniture.actionCode)

        defineFurniture.parts.forEach {
            it.writeTo(buffer)
        }

        return buffer
    }

    override fun toString(): String {
        return "PlaceFurnitureResult(placeFurniture=$placeFurniture, defineFurniture=$defineFurniture, partLength=$partLength)"
    }
}