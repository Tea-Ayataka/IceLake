package net.ayataka.marinetooler.pigg.network.packet.data.place

class PlaceFurniture : PlaceData() {
    var sequence = 0
    var ownerId = ""

    override fun toString(): String {
        return "PlaceFurniture(sequence=$sequence, ownerId='$ownerId', characterId='$characterId')"
    }
}