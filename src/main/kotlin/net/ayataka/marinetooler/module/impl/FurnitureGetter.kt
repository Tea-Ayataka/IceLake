package net.ayataka.marinetooler.module.impl

import com.google.gson.Gson
import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.StockFurniture
import net.ayataka.marinetooler.pigg.network.packet.recv.ActionResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.TalkResultPacket
import java.awt.Color
import java.io.File
import kotlin.concurrent.timer

object FurnitureGetter : Module() {
    private val gson = Gson()
    private val file = File("furnitures").apply {
        if(!exists()){
            createNewFile()
        }
    }

    val furnitures = gson.fromJson(file.readText(), Array<StockFurniture>::class.java)?.toMutableList() ?: mutableListOf()

    init {
        timer(period = 60000){
            file.writeText(gson.toJson(furnitures))
        }
    }

    @EventListener
    fun onRecvPacket(event: RecvPacketEvent) {
        val packet = event.packet

        if (packet is BaseAreaData) {
            furnitures.addAll(packet.defineFurnitures.map {
                StockFurniture().apply {
                    characterId = it.characterId
                    type = it.type
                    category = it.category
                    name = it.name
                    description = it.description
                    actionCode = it.actionCode

                    sizeX = it.sizeX
                    sizeY = it.sizeY

                    parts = it.parts
                }
            }.filter { furniture -> furnitures.none { it.characterId == furniture.characterId } })
        }
    }
}