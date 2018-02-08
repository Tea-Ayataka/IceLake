package net.ayataka.marinetooler.proxy.websocket

import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.proxy.websocket.proxy.WClient
import net.ayataka.marinetooler.proxy.websocket.proxy.WServer
import org.java_websocket.server.DefaultSSLWebSocketServerFactory
import javax.net.ssl.SSLContext

class WebSocketProxy(ip: String, port: Int, val remoteUri: String, val packetListener: IPacketListener?, ssl: SSLContext) {
    val server = WServer(ip, port, this)
    var client: WClient? = null

    init {
        // Start WServer with SSL certificate
        this.server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))
        this.server.start()
    }

    fun stop() {
        this.client?.close()
        this.server.stop()
    }

    fun send(packet: Packet) {
        val data = packet.write()
        println("[WS SEND (FORCED)] ${data?.array()?.size} bytes")
        println(data?.array()?.toHexString())
        this.client?.send(data)
    }

    fun receive(packet: Packet) {
        val data = packet.write()
        println("[WS RECV (FORCED)] ${data?.array()?.size} bytes")
        println(data?.array()?.toHexString())
        this.server.broadcast(data?.array())
    }
}