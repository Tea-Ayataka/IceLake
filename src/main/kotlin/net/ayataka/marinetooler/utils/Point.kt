package net.ayataka.marinetooler.utils

class Point {
    constructor(x: Float, y: Float){
        x = x
        y = y
    }

    constructor()

    var x: Float = 0F
    var y: Float = 0F

    fun toArray(): Array<Float>{
        return arrayOf(x,  y)
    }
}