package net.ayataka.marinetooler.pigg.network.packet.data.define

import net.ayataka.marinetooler.pigg.network.packet.data.player.PetData

class DefinePet {
    var data = PetData()

    fun getPetID(): Int{
        return data.petId
    }
}