package net.ayataka.marinetooler.utils

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import java.util.zip.DeflaterInputStream
import java.util.zip.InflaterInputStream

fun ByteArray.decompress(): ByteBuilder {
    return InflaterInputStream(this.inputStream()).use {
        it.readBytes()
    }.let { ByteBuilder(it) }
}

fun ByteBuilder.compress(): ByteArray {
    return DeflaterInputStream(this.build().array().inputStream()).use {
        it.readBytes()
    }
}