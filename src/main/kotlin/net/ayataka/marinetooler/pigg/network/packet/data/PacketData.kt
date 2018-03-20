package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

interface PacketData {
    fun readFrom(buffer: ByteBuilder)
    fun writeTo(buffer: ByteBuilder)
}