package net.ayataka.marinetooler.icearea

import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceActionItem
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture

class IceAreaData {
    val defineFurnitures: MutableList<DefineFurniture> = mutableListOf()
    val placeFurnitures: MutableList<PlaceFurniture> = mutableListOf()
    val placeActionItems: MutableList<PlaceActionItem> = mutableListOf()
    var areaData: AreaData = AreaData()
    val opUsers: MutableList<String> = mutableListOf()
}