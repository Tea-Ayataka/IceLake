package net.ayataka.marinetooler.module.impl

import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.send.ChangeWindowAquariumPacket

object AquariumSpammer : Module() {
    fun addFish(fishName: String){
        val packet = ChangeWindowAquariumPacket()

        packet.method = "onUpdateWindowFish"

        val data = ByteBuilder()
                .writeString("fishing_window_aquarium_special_river_001")
                .writeBoolean(true)
                .writeString(fishName).build().array()

        packet.data = data

        Pigg.send(packet)
    }
}