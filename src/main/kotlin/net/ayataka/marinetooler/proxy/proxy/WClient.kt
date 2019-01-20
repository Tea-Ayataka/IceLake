package net.ayataka.marinetooler.proxy.proxy

import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.error
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.utils.trace
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer

class WClient(remoteUri: String, private val proxy: WebSocketProxy) : WebSocketClient(URI(remoteUri)) {
    init {
        connectionLostTimeout = 0
    }

    override fun onOpen(handshake: ServerHandshake?) {
        info("[WS CLIENT] Client connected to $uri")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        info("[WS CLIENT] Disconnected from remote server (code: $code, reason: $reason, remote: $remote)")
        proxy.server.connections.forEach { it.close(code, reason) }
        proxy.server.stop()
    }

    override fun onMessage(message: String?) {
        trace("[WS RECV] : $message")

        proxy.server.broadcast(message)
    }

    override fun onMessage(message: ByteBuffer?) {
        if (message == null) {
            return
        }

        trace("[WS RECV] ${message.array().size} bytes")
        trace(message.array().toHexString())

        val modified = proxy.onReceive(message) ?: return
        proxy.server.broadcast(modified.array())
    }

    override fun onError(ex: Exception?) {
        error("[WS CLIENT] An error occurred on connection : $ex")
        ex?.printStackTrace()
    }

    init {
        info("[WS CLIENT] Initialized")
    }
}