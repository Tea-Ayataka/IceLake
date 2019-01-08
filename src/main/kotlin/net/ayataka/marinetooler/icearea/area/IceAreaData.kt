package net.ayataka.marinetooler.icearea.area

import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceActionItem
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture

class IceAreaData(val areaCode: String, val areaName: String) {
    var areaData: AreaData = AreaData().apply {
        categoryCode = "secret"
        categoryName = "Iceエリア"
        areaName = this@IceAreaData.areaName
        areaCode = this@IceAreaData.areaCode

        sizeX = 16
        sizeY = 16

        wallCode = "casino_bunny_wall_1201"
        floorCode = "quizlive_1805_floor"
        frontCode = "hawaiibeach_extr_sky_1708"
    }

    val defineFurnitures: MutableList<DefineFurniture> = mutableListOf()
    val placeFurnitures: MutableList<PlaceFurniture> = mutableListOf()
    val placeActionItems: MutableList<PlaceActionItem> = mutableListOf()
    val opUsers: MutableList<String> = mutableListOf()
}