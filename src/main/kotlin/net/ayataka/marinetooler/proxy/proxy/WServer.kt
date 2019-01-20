package net.ayataka.marinetooler.proxy.proxy

import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.utils.error
import net.ayataka.marinetooler.utils.trace
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class WServer(private val ip: String, private val prt: Int, private val proxy: WebSocketProxy, private val onDisconnected: () -> Unit? = {}) : WebSocketServer(InetSocketAddress(ip, prt)) {
    init {
        connectionLostTimeout = 0
    }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        info("[WS SERVER] New websocket connection with ${handshake!!.resourceDescriptor}")

        // Initialize and start client
        proxy.client = WClient(proxy.remoteUri, proxy)
        proxy.client!!.connect()
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        info("[WS SERVER] Disconnected. (code: $code, reason: $reason, remote: $remote)")
        proxy.client!!.close()
        onDisconnected.invoke()
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        trace("[WS SEND] : $message")
        proxy.client!!.send(message)
    }

    override fun onMessage(conn: WebSocket, message: ByteBuffer) {
        trace("[WS SEND] ${message.array().size} bytes")
        trace(message.array().toHexString())

        val modified = proxy.onSend(message) ?: return
        proxy.client!!.send(modified)
    }

    override fun onStart() {
        info("[WS SERVER] Server started on $ip:$prt")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        error("[WS SERVER] An error occurred on connection : $ex")
        ex?.printStackTrace()
    }
}