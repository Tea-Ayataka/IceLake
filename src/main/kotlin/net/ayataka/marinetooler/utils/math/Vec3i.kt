package net.ayataka.marinetooler.utils.math

class Vec3i(var x: Int = 0, var y: Int = 0, var z: Int = 0) {
    fun clone(): Vec3i {
        return Vec3i(x, y, z)
    }

    override fun toString(): String {
        return "X: $x, Y: $y, Z: $z"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vec3i

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        result = 31 * result + z
        return result
    }
}