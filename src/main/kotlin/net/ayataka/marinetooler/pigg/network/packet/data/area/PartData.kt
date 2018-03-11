package net.ayataka.marinetooler.pigg.network.packet.data.area

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class PartData(val param2: Boolean) : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.NONE.id

    var height: Int = 0
    var walkable = false
    var sittable = false
    var orgSittable = false
    var attachable = false
    var attachDirection: Byte = -1
    var rx: Byte = 0
    var ry: Byte = 0
    var index: Int = 0
    var facing: Facing = Facing.walls[0]

    override fun readFrom(buffer: ByteBuilder) {
        this.height = buffer.readInt()
        this.attachable = buffer.readBoolean()
        this.sittable = buffer.readBoolean()
        this.walkable = buffer.readBoolean()

        val test = buffer.readByte().toInt()

        dump(test.toString())

        val facing = buffer.readByte()
        dump(facing.toString())
        this.facing = Facing.walls[facing.toInt()]

        if(param2){
            this.attachDirection = buffer.readByte()
        }

        this.rx = buffer.readByte()
        this.ry = buffer.readByte()
        this.orgSittable = buffer.readBoolean()
    }

    fun clone() : PartData{
        return this
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }
}