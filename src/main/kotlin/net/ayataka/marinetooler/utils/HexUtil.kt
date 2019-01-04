package net.ayataka.marinetooler.utils


fun ByteArray.toHexString() = joinToString(" ") { it.toUByte().toString(16).padStart(2, '0') }

fun String.fromHexToBytes() = replace(" ", "").chunked(2).map { it.toUByte(16).toByte() }.toByteArray()