package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.puzzle.PuzzleUserItemData
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.trace

class GetPuzzleUserStatusResult : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.GET_USER_PUZZLE_STATUS_RESULT.id

    var status = ""
    var equippedScoreItemCategory = ""
    var scoreItems: List<PuzzleUserItemData> = listOf()
    var cheatItems: List<PuzzleUserItemData> = listOf()
    var hasLimitedTermIncentive = false
    var hasLimitedQuantityIncentive = false
    var hasEvent = false
    var eventCode = ""
    var eventPoint = 0
    var eventWinningStreakCount = 0
    var hasTheme = false
    var boardCode = ""
    var boardUUID = ""
    var boardBaseData = ""
    var boardData = ""
    var boardPoint = 0
    var boardRemainingMoveCount = 0
    var isScoreItemUsing = false
    var boardUsingPuzzleScoreItemCategory = ""
    var boardUsingPuzzleScoreItemMagnification = 0
    var progressCount = 0

    override fun readFrom(buffer: ByteBuilder) {
        status = buffer.readString()
        equippedScoreItemCategory = buffer.readString()

        val scoreItemCount = buffer.readInt()
        trace("ScoreItems: $scoreItemCount")
        scoreItems = (0 until scoreItemCount).map {
            PuzzleUserItemData().apply { readFrom(buffer) }
        }

        val cheatItemCount = buffer.readInt()
        trace("CheatItems: $cheatItemCount")
        cheatItems = (0 until cheatItemCount).map {
            PuzzleUserItemData().apply { readFrom(buffer, false) }
        }

        hasEvent = buffer.readBoolean()
        if (hasEvent) {
            eventCode = buffer.readString()
            eventPoint = buffer.readInt()
            eventWinningStreakCount = buffer.readInt()
            hasLimitedTermIncentive = buffer.readBoolean()
            hasLimitedQuantityIncentive = buffer.readBoolean()
        }

        hasTheme = buffer.readBoolean()
        if (hasTheme) {
            boardCode = buffer.readString()
            boardUUID = buffer.readString()
            boardBaseData = buffer.readString()
            boardData = buffer.readString()
            boardPoint = buffer.readInt()
            boardRemainingMoveCount = buffer.readInt()
            isScoreItemUsing = buffer.readBoolean()
            if (isScoreItemUsing) {
                boardUsingPuzzleScoreItemCategory = buffer.readString()
                boardUsingPuzzleScoreItemMagnification = buffer.readInt()
            }
            progressCount = buffer.readInt()
        }

        trace(toString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(status)
        buffer.writeString(equippedScoreItemCategory)

        buffer.writeInt(scoreItems.size)
        scoreItems.forEach { it.writeTo(buffer) }

        buffer.writeInt(cheatItems.size)
        cheatItems.forEach { it.writeTo(buffer, false) }

        buffer.writeBoolean(hasEvent)
        if (hasEvent) {
            buffer.writeString(eventCode)
            buffer.writeInt(eventPoint)
            buffer.writeInt(eventWinningStreakCount)
            buffer.writeBoolean(hasLimitedTermIncentive)
            buffer.writeBoolean(hasLimitedQuantityIncentive)
        }

        buffer.writeBoolean(hasTheme)
        if (hasTheme) {
            buffer.writeString(boardCode)
            buffer.writeString(boardUUID)
            buffer.writeString(boardBaseData)
            buffer.writeString(modify(boardData))
            trace("Modified BoardData: ${modify(boardData)}")
            buffer.writeInt(boardPoint)
            buffer.writeInt(boardRemainingMoveCount)
            buffer.writeBoolean(isScoreItemUsing)
            if (isScoreItemUsing) {
                buffer.writeString(boardUsingPuzzleScoreItemCategory)
                buffer.writeInt(boardUsingPuzzleScoreItemMagnification)
            }
            buffer.writeInt(progressCount)
        }

        // HAX
        buffer.writeBytes("00 00 00 00 00 00 00 00 00".fromHexToBytes())

        return buffer
    }

    fun modify(data: String): String {
        var types = data.split("_")[0]
        var jellies = data.split("_")[1]
        val param2 = data.split("_")[2]
        val param3 = data.split("_")[3]
        val param4 = data.split("_")[4]

        jellies = jellies.replace("[\\d-]".toRegex(), "0")
        types = types.replace(",1,", ",5,")

        return types + "_" + jellies + "_" + param2 + "_" + param3 + "_" + param4
    }

    override fun toString(): String {
        return "GetPuzzleUserStatusResult(" +
                "status='$status',\n" +
                "equippedScoreItemCategory='$equippedScoreItemCategory',\n" +
                "scoreItems=$scoreItems,\n" +
                "cheatItems=$cheatItems,\n" +
                "hasLimitedTermIncentive=$hasLimitedTermIncentive,\n" +
                "hasLimitedQuantityIncentive=$hasLimitedQuantityIncentive,\n" +
                "hasEvent=$hasEvent,\n" +
                "eventCode='$eventCode',\n" +
                "eventPoint=$eventPoint,\n" +
                "eventWinningStreakCount=$eventWinningStreakCount,\n" +
                "hasTheme=$hasTheme,\n" +
                "boardCode='$boardCode',\n" +
                "boardUUID='$boardUUID',\n" +
                "boardBaseData='$boardBaseData',\n" +
                "boardData='$boardData',\n" +
                "boardPoint=$boardPoint,\n" +
                "boardRemainingMoveCount=$boardRemainingMoveCount,\n" +
                "isScoreItemUsing=$isScoreItemUsing,\n" +
                "boardUsingPuzzleScoreItemCategory='$boardUsingPuzzleScoreItemCategory',\n" +
                "boardUsingPuzzleScoreItemMagnification=$boardUsingPuzzleScoreItemMagnification,\n" +
                "progressCount=$progressCount" +
                ")"
    }
}