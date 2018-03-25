package net.ayataka.marinetooler.proxy.websocket.proxy

import net.ayataka.marinetooler.proxy.websocket.WebSocketProxy
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.utils.warn
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.lang.Exception
import java.net.InetSocketAddress
import java.nio.ByteBuffer

class WServer(private val ip: String, private val prt: Int, private val proxy: WebSocketProxy, private val onDisconnected: () -> Unit? = {}) : WebSocketServer(InetSocketAddress(ip, prt)) {
    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        println("[WS SERVER] New WebSocket connection with ${handshake!!.resourceDescriptor}")

        // Initialize and start client
        proxy.client = WClient(proxy.remoteUri, proxy)
        proxy.client!!.connect()
    }

    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {
        println("[WS SERVER] Disconnected.")
        proxy.client!!.close()
        onDisconnected.invoke()
    }

    override fun onMessage(conn: WebSocket?, message: String?) {
        println("[WS SEND] : $message")
        proxy.client!!.send(message)
    }

    override fun onMessage(conn: WebSocket, message: ByteBuffer) {
        println("[WS SEND] ${message.array().size} bytes")
        println(message.array().toHexString())

        var data: ByteBuffer = message

        // Fire packet event
        proxy.packetListener?.let {
            data = it.send(data) ?: return // Canceled packet will be null.
        }

        proxy.client!!.send(data)
    }

    override fun onStart() {
        info("[WS SERVER] Server started on $ip:$prt")
    }

    override fun onError(conn: WebSocket?, ex: Exception?) {
        warn("[WS SERVER] An error occurred on connection : $ex")
        ex?.printStackTrace()
    }
}