package net.ayataka.marinetooler.pigg.network.packet.data.area

class StockFurniture {
    var uniqueId: Int = 0
    var sequence: Int = 0
    var characterId = ""
    var ownerId = ""
    var name = ""
    var description = ""
    var type: Byte = 0
    var category = ""
    var actionCode = ""
    var parts: MutableList<PartData> = mutableListOf()
    var using = false
    var sizeX: Int = 0
    var sizeY: Int = 0
    var quantity: Int = 0
    var used: Int = 0
    var time: Double = 0.0
    var canUse = false
}