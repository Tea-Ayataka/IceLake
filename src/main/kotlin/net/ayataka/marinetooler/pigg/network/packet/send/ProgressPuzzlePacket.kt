package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class ProgressPuzzlePacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PROGRESS_PUZZLE.id
    override val encrypted = true

    var isClear = false
    var point = 0
    var boardData = ""
    var needleGummyCreateCount = 0
    var rainbowGummyCreateCount = 0
    var arrowGummyCreateCount = 0
    var isDecrementMoveCount = false
    var additionalMissionPoint = 0
    var additionalAreaPoint = 0

    override fun readFrom(buffer: ByteBuilder) {
        this.isClear = buffer.readBoolean()
        this.point = buffer.readInt()
        this.boardData = buffer.readString()
        this.needleGummyCreateCount = buffer.readInt()
        this.rainbowGummyCreateCount = buffer.readInt()
        this.arrowGummyCreateCount = buffer.readInt()
        this.isDecrementMoveCount = buffer.readBoolean()

        dump("BoardData: $boardData")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeBoolean(this.isClear)
        buffer.writeInt(this.point)
        buffer.writeString(this.boardData)
        buffer.writeInt(this.needleGummyCreateCount)
        buffer.writeInt(this.rainbowGummyCreateCount)
        buffer.writeInt(this.arrowGummyCreateCount)
        buffer.writeBoolean(this.isDecrementMoveCount)
        buffer.writeInt(this.additionalMissionPoint)
        buffer.writeInt(this.additionalAreaPoint)
        buffer.writeDoubleTimeStamp()
        return buffer
    }
}