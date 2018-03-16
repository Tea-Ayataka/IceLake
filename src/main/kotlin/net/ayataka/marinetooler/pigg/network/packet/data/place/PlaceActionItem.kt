package net.ayataka.marinetooler.pigg.network.packet.data.place

class PlaceActionItem {
    var sequence = 0
    var itemCode = ""
    var itemType = ""
    var ownerCode = ""
    var actionItemType = 2
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0

    fun clone() : PlaceActionItem{
        return this
    }
}