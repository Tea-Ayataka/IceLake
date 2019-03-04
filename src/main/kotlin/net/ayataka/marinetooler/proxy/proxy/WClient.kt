package net.ayataka.marinetooler.proxy.proxy

import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer

class WClient(remoteUri: String, private val proxy: WebSocketProxy) : WebSocketClient(URI(remoteUri)) {
    init {
        connectionLostTimeout = 0
    }

    override fun onOpen(handshake: ServerHandshake?) = proxy.start {
        info("[WS CLIENT] Client connected to $uri")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) = proxy.start {
        info("[WS CLIENT] Disconnected from remote server (code: $code, reason: $reason, remote: $remote)")
        proxy.server.connections().forEach { it.close(code, reason) }
        proxy.server.stop()
    }

    override fun onMessage(message: String?) = proxy.start {
        trace("[WS RECV] : $message")

        proxy.server.broadcast(message)
    }

    override fun onMessage(message: ByteBuffer) = proxy.start {
        val modified = proxy.onReceive(message) ?: return@start

        trace("[WS RECV] ${modified.array().size} bytes\n${modified.array().toHexString()}")

        proxy.server.broadcast(modified.array())
    }

    override fun onError(ex: Exception?) = proxy.start {
        error("[WS CLIENT] An error occurred on connection : $ex")
        ex?.printStackTrace()
    }
}