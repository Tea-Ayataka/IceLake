package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.puzzle.PuzzleIncentiveItemData
import net.ayataka.marinetooler.utils.trace

class ProgressPuzzleResultPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PROGRESS_PUZZLE_RESULT.id

    var status = ""
    var isThemeClear = false
    var themePoint = 0
    var winningStreakPoint = 0
    var isStepClear = false
    var step = 0
    var incentiveItemVec: MutableList<List<PuzzleIncentiveItemData>> = mutableListOf()
    var limitedTermIncentiveItemData: List<PuzzleIncentiveItemData> = listOf()
    var limitedQuantityIncentiveItemData: List<PuzzleIncentiveItemData> = listOf()
    var isProvidedLimitedTermIncentive = false
    var isProvidedLimitedQuantityIncentive = false

    override fun readFrom(buffer: ByteBuilder) {
        status = buffer.readString()
        if (status != "SUCCESS") {
            return
        }

        isThemeClear = buffer.readBoolean()

        if (isThemeClear) {
            themePoint = buffer.readInt()
            winningStreakPoint = buffer.readInt()

            isStepClear = buffer.readBoolean()
            if (isStepClear) {
                repeat(buffer.readInt()) {
                    step = buffer.readInt()
                    incentiveItemVec[it] = (0 until buffer.readInt()).map {
                        PuzzleIncentiveItemData().apply { readFrom(buffer) }
                    }
                }
            }

            isProvidedLimitedTermIncentive = buffer.readBoolean()
            if (isProvidedLimitedTermIncentive) {
                repeat(buffer.readInt()) {
                    limitedTermIncentiveItemData = (0 until buffer.readInt()).map {
                        PuzzleIncentiveItemData().apply { readFrom(buffer) }
                    }
                }
            }

            isProvidedLimitedQuantityIncentive = buffer.readBoolean()
            if (isProvidedLimitedQuantityIncentive) {
                repeat(buffer.readInt()) {
                    limitedQuantityIncentiveItemData = (0 until buffer.readInt()).map {
                        PuzzleIncentiveItemData().apply { readFrom(buffer) }
                    }
                }
            }
        }

        trace(toString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        return null
    }

    override fun toString(): String {
        return "ProgressPuzzleResultPacket(status='$status', isThemeClear=$isThemeClear, themePoint=$themePoint, winningStreakPoint=$winningStreakPoint, isStepClear=$isStepClear, step=$step, incentiveItemVec=$incentiveItemVec, limitedTermIncentiveItemData=$limitedTermIncentiveItemData, limitedQuantityIncentiveItemData=$limitedQuantityIncentiveItemData, isProvidedLimitedTermIncentive=$isProvidedLimitedTermIncentive, isProvidedLimitedQuantityIncentive=$isProvidedLimitedQuantityIncentive)"
    }
}