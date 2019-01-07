package net.ayataka.marinetooler.pigg.network.packet.data.area

import com.google.gson.annotations.Expose

class StockFurniture {
    var uniqueId: Int = 0
    var sequence: Int = 0
    @Expose
    var characterId = ""
    var ownerId = ""
    @Expose
    var name = ""
    var description = ""
    var type: Byte = 0
    @Expose
    var category = ""
    var actionCode = ""
    @Expose
    var parts: MutableList<PartData> = mutableListOf()
    var using = false
    var sizeX: Int = 0
    var sizeY: Int = 0
    var quantity: Int = 0
    var used: Int = 0
    var time: Double = 0.0
    var canUse = false

    override fun toString(): String {
        return "StockFurniture(uniqueId=$uniqueId, sequence=$sequence, characterId='$characterId', ownerId='$ownerId', name='$name', description='$description', type=$type, category='$category', actionCode='$actionCode', parts=$parts, using=$using, sizeX=$sizeX, sizeY=$sizeY, quantity=$quantity, used=$used, time=$time, canUse=$canUse)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StockFurniture) return false

        if (characterId != other.characterId) return false

        return true
    }

    override fun hashCode(): Int {
        return characterId.hashCode()
    }
}