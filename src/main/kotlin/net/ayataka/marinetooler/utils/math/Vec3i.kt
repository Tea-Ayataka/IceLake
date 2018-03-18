package net.ayataka.marinetooler.utils.math

class Vec3i(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    fun clone(): Vec3i {
        return Vec3i(this.x, this.y, this.z)
    }

    override fun toString(): String {
        return "X: ${this.x}, Y: ${this.y}, Z: ${this.z}"
    }

    override fun equals(other: Any?): Boolean {
        if (other == this) {
            return true
        }

        if (other !is Vec3i) {
            return false
        }

        return other.x == this.x && other.y == this.y && other.z == this.z
    }
}