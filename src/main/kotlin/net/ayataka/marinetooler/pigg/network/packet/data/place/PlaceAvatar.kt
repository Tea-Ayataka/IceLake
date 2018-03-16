package net.ayataka.marinetooler.pigg.network.packet.data.place

class PlaceAvatar : PlaceData() {
    var tired: Byte = 0
    var mode: Byte = 0

    fun clone() : PlaceAvatar{
        return this
    }
}