package net.ayataka.marinetooler.utils

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import java.util.zip.Deflater
import java.util.zip.Inflater

fun ByteArray.decompress(): ByteBuilder {
    val decompressedLength = ByteBuilder(this).readInt()
    val result = ByteArray(decompressedLength)

    Inflater().apply {
        setInput(this@decompress)
    }.inflate(result)

    return ByteBuilder(result)
}

fun ByteBuilder.compress(): ByteArray {
    val input = build().array()

    val deflater = Deflater().apply {
        setInput(input)
        finish()
    }

    var output = ByteArray(input.size)
    val size = deflater.deflate(output)
    output = output.take(size).toTypedArray().toByteArray()

    return output
}