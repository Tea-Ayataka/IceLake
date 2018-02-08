package net.ayataka.marinetooler.proxy.websocket

import java.nio.ByteBuffer

interface IPacketListener {
    fun send(buffer: ByteBuffer): ByteBuffer?
    fun receive(buffer: ByteBuffer): ByteBuffer?
}