package net.ayataka.marinetooler.utils.math

class Vec3i(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    fun clone(): Vec3i {
        return Vec3i(this.x, this.y, this.z)
    }

    override fun toString(): String {
        return "X: ${this.x}, Y: ${this.y}, Z: ${this.z}"
    }

    fun equals(another: Vec3i): Boolean {
        return another.x == this.x && another.y == this.y && another.z == this.z
    }
}