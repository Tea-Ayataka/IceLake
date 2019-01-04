package net.ayataka.marinetooler.proxy

import net.ayataka.marinetooler.proxy.proxy.WClient
import net.ayataka.marinetooler.proxy.proxy.WServer
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import org.java_websocket.server.DefaultSSLWebSocketServerFactory
import java.nio.ByteBuffer
import javax.net.ssl.SSLContext
import kotlin.concurrent.timer

class WebSocketProxy(
        private val ip: String,
        val port: Int,
        val remoteUri: String,
        val packetListener: IPacketListener?,
        val ssl: SSLContext,
        val fireEvents: Boolean = false
) {
    var server: WServer
    var client: WClient? = null

    init {
        server = WServer(ip, port, this) { startServer() }
        server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))
        server.start()
    }

    private fun startServer() {
        // HAX: BugFix
        timer(initialDelay = 200, period = Long.MAX_VALUE) {
            this.cancel()
            server.stop()
            server = WServer(ip, port, this@WebSocketProxy) { startServer() }
            server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))
            server.start()
        }
    }

    fun stop() {
        client?.close()

        server.connections().forEach { it.close(2000, "Disconnect") }
        //server.stop()
        info("Disconnected Clients")
    }

    fun send(data: ByteBuffer) {
        println("[WS SEND (FORCED)] ${data.array().size} bytes")
        println(data.array().toHexString())
        client?.send(data.array())
    }

    fun receive(data: ByteBuffer) {
        println("[WS RECV (FORCED)] ${data.array().size} bytes")
        println(data.array().toHexString())
        server.broadcast(data.array())
    }
}