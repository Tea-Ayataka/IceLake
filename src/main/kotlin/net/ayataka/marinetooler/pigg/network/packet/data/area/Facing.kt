package net.ayataka.marinetooler.pigg.network.packet.data.area

class Facing(var north: Int, var east: Int, var west:Int, var south:Int){
    companion object {
         val walls = arrayOf(Facing(0, 0, 0, 0), (Facing(0, 0, 0, 1)), Facing(0, 0, 1, 0), Facing(0, 0, 1, 1), Facing(0, 1, 0, 0), Facing(0, 1, 0, 1), Facing(0, 1, 1, 0), Facing(0, 1, 1, 1), Facing(1, 0, 0, 0), Facing(1, 0, 0, 1), Facing(1, 0, 1, 0), Facing(1, 0, 1, 1), Facing(1, 1, 0, 0), Facing(1, 1, 0, 1), Facing(1, 1, 1, 0), Facing(1, 1, 1, 1))
    }
}
