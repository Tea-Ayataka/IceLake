package net.ayataka.marinetooler.utils

import org.apache.commons.codec.binary.Hex

fun ByteArray.toHexString(): String {
    val builder = StringBuilder()

    for (byte in this) {
        builder.append(Hex.encodeHexString(byteArrayOf(byte)))
        builder.append(" ")
    }

    return builder.toString()
}

fun String.fromHexToBytes(): ByteArray {
    val hex = replace(" ", "")
    return Hex.decodeHex(hex.toCharArray())
}