package net.ayataka.marinetooler.proxy.proxy

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.*
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class WServer(private val ip: String, private val prt: Int, private val proxy: WebSocketProxy, private val onDisconnected: () -> Unit? = {}) : WebSocketServer(InetSocketAddress(ip, prt)) {
    init {
        connectionLostTimeout = 0
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) = proxy.start {
        info("[WS SERVER] New websocket connection with ${handshake!!.resourceDescriptor}")

        // Initialize and start client
        GlobalScope.launch {
            proxy.client = WClient(proxy.remoteUri, proxy)
            proxy.client!!.connectBlocking()
        }
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) = proxy.start {
        info("[WS SERVER] Disconnected. (code: $code, reason: $reason, remote: $remote)")
        proxy.client!!.close()
        onDisconnected.invoke()
    }

    override fun onMessage(conn: WebSocket, message: String) = proxy.start {
        proxy.client!!.send(message)
    }

    override fun onMessage(conn: WebSocket, message: ByteBuffer) = proxy.start {
        val modified = proxy.onSend(message) ?: return@start

        trace("[WS SEND] ${modified.array().size} bytes\n${modified.array().toHexString()}")

        proxy.client!!.send(modified)
    }

    override fun onStart() = proxy.start {
        info("[WS SERVER] Server started on $ip:$prt")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) = proxy.start {
        error("[WS SERVER] An error occurred on connection", ex)
        ex?.printStackTrace()
    }
}