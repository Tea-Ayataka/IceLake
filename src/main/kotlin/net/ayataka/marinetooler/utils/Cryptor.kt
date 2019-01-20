package net.ayataka.marinetooler.utils


private val CIPHER_KEY = byteArrayOf(1, 1, 4, 5, 1, 4, 1, 9, 1, 9, 8, 10)

fun ByteArray.cryptXor() : ByteArray {
    for (i in 0 until size) {
        this[i] = (this[i].toInt() xor CIPHER_KEY[i % CIPHER_KEY.size] * (i - 2 % 16)).toByte()
    }

    return this
}