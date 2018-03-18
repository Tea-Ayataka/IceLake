package net.ayataka.marinetooler.pigg.network.packet.data.define

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.player.AvatarData

class DefineAvatar : DefineData() {
    var part = PartData(false)
    var data = AvatarData()
    var friend = false

    fun readData(buffer: ByteBuilder){
        data = AvatarData()

        data.readFrom(buffer)
        friend = false
    }

    fun writeData(buffer: ByteBuilder): ByteBuilder{
        return data.writeTo(buffer)
    }

    fun clone(): DefineAvatar{
        return this
    }
}