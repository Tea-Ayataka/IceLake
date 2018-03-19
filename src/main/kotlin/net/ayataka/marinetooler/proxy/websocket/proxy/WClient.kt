package net.ayataka.marinetooler.proxy.websocket.proxy

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.event.ConnectEvent
import net.ayataka.marinetooler.pigg.event.DisconnectEvent
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.warn
import net.ayataka.marinetooler.proxy.websocket.WebSocketProxy
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.nio.ByteBuffer

class WClient(remoteUri: String, private val proxy: WebSocketProxy) : WebSocketClient(URI(remoteUri)) {
    override fun onOpen(handshake: ServerHandshake?) {
        println("[WS CLIENT] Client Connected to $uri")
        EventManager.fire(ConnectEvent())
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("[WS CLIENT] Disconnected from remote server")
        EventManager.fire(DisconnectEvent())
    }

    override fun onMessage(message: String?) {
        println("[WS RECV] : $message")

        proxy.server.broadcast(message)
    }

    override fun onMessage(message: ByteBuffer?) {
        if (message == null) {
            return
        }

        println("[WS RECV] : ${message.array().size} bytes")

        var data: ByteBuffer = message

        // Fire packet event
        proxy.packetListener?.let {
            data = it.receive(data) ?: return // Canceled packet will be null.
        }

        proxy.server.broadcast(data.array())
    }

    override fun onError(ex: Exception?) {
        warn("[WS CLIENT] An error occurred on connection : $ex")
        ex?.printStackTrace()
    }

    init {
        info("[WS CLIENT] Initialized")
    }
}