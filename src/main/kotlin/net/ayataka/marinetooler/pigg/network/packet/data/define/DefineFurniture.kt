package net.ayataka.marinetooler.pigg.network.packet.data.define

import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData

class DefineFurniture : DefineData() {
    var parts = mutableListOf<PartData>()

    var sizeX = 0
    var sizeY = 0

    var category = ""

    var type: Byte = 0

    var description = ""
    var actionCode = ""
}