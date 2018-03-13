package net.ayataka.marinetooler.pigg.network.packet.data.place

class PlaceAvatar : PlaceData() {
    var tired = 0
    var mode = 0

    fun clone() : PlaceAvatar{
        return this
    }
}