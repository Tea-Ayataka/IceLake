package net.ayataka.marinetooler.emulator.pigg

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.event.ConnectEvent
import net.ayataka.marinetooler.pigg.event.DisconnectEvent
import net.ayataka.marinetooler.proxy.websocket.IPacketListener
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.utils.warn
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.net.URI
import java.nio.ByteBuffer

class VClient(remoteUri: String, val packetListener: IPacketListener?) : WebSocketClient(URI(remoteUri)) {
    override fun onOpen(handshake: ServerHandshake?) {
        println("[VWS CLIENT] Client Connected to $uri")
        EventManager.fire(ConnectEvent())
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        println("[VWS CLIENT] Disconnected from remote server")
        EventManager.fire(DisconnectEvent())
    }

    override fun onMessage(message: String?) {
        println("[VWS RECV] : $message")
    }

    override fun onMessage(message: ByteBuffer?) {
        if (message == null) {
            return
        }

        println("[VWS RECV] : ${message.array().size} bytes")
        println(message.array().toHexString())

        packetListener?.receive(message)
    }

    override fun onError(ex: Exception?) {
        warn("[WS CLIENT] An error occurred on connection : $ex")
        ex?.printStackTrace()
    }

    init {
        info("[WS CLIENT] Initialized")
    }
}