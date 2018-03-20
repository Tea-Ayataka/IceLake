package net.ayataka.marinetooler.pigg.network.packet.data.place

class PlaceActionItem {
    var sequence = 0
    var itemCode = ""
    var itemType = ""
    var ownerCode = ""
    var actionItemType: Byte = 2
    var x: Short = 0
    var y: Short = 0
    var z: Short = 0

    var mode = 0
}