package net.ayataka.marinetooler.module.impl

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.StockFurniture
import net.ayataka.marinetooler.pigg.network.packet.recv.ListClubFurnitureResult
import net.ayataka.marinetooler.pigg.network.packet.recv.ListUserFurnitureResultPacket
import java.io.File

object FurnitureGetter : Module() {
    private val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()
    private val file = File("furnitures.json").apply {
        if (!exists()) {
            createNewFile()
        }
    }

    val database = gson.fromJson(file.readText(), AreaDatabase::class.java) ?: AreaDatabase()

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is BaseAreaData) {
            database.furnitures.addAll(packet.defineFurnitures.map {
                StockFurniture().apply {
                    characterId = it.characterId
                    category = it.category
                    name = it.name

                    parts = it.parts
                }
            })

            database.walls.add(packet.areaData.wallCode)
            database.windows.add(packet.areaData.windowCode)
            database.floors.add(packet.areaData.floorCode)
            database.fronts.add(packet.areaData.frontCode)

            file.writeText(gson.toJson(database))
        }

        if (packet is ListClubFurnitureResult) {
            database.furnitures.addAll(packet.furnitures)

            file.writeText(gson.toJson(database))
        }

        if (packet is ListUserFurnitureResultPacket) {
            database.furnitures.addAll(packet.furnitures)

            file.writeText(gson.toJson(database))
        }
    }
}

class AreaDatabase {
    @Expose
    val furnitures = mutableSetOf<StockFurniture>()
    @Expose
    val walls = mutableSetOf<String>()
    @Expose
    val windows = mutableSetOf<String>()
    @Expose
    val floors = mutableSetOf<String>()
    @Expose
    val fronts = mutableSetOf<String>()
}