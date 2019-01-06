package net.ayataka.marinetooler.icearea

import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceActionItem
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import java.util.*

data class IceAreaData(val defineFurnitures: MutableList<DefineFurniture> = mutableListOf(),
                  val placeFurnitures: MutableList<PlaceFurniture> = mutableListOf(),
                  val placeActionItems: MutableList<PlaceActionItem> = mutableListOf(),
                  val areaData: MutableMap<String, String> = mutableMapOf("wallCode" to "small_basic_wall", "floorCode" to "small_basic_floor", "windowCode" to "small_basic_window", "areaName" to "IceArea", "sizeX" to "16", "sizeY" to "16"))