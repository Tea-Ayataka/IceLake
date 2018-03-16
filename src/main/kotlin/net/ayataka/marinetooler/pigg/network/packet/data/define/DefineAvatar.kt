package net.ayataka.marinetooler.pigg.network.packet.data.define

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.player.AvatarData

class DefineAvatar : DefineData() {
    var part = PartData(false)
    var data = AvatarData()
    var friend = false

    fun readData(buffer: ByteBuilder){
        this.data = AvatarData()

        this.data.readFrom(buffer)
        this.friend = false
    }

    fun writeData(buffer: ByteBuilder): ByteBuilder{
        return this.data.writeTo(buffer)
    }

    fun clone(): DefineAvatar{
        return this
    }
}