package net.ayataka.marinetooler.proxy.websocket

import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.proxy.websocket.proxy.WClient
import net.ayataka.marinetooler.proxy.websocket.proxy.WServer
import net.ayataka.marinetooler.utils.toHexString
import org.java_websocket.server.DefaultSSLWebSocketServerFactory
import javax.net.ssl.SSLContext
import kotlin.concurrent.timer

class WebSocketProxy(
        private val ip: String,
        private val port: Int,
        val remoteUri: String,
        val packetListener: IPacketListener?,
        private val ssl: SSLContext,
        private val usePolicyServer: Boolean = false
) {
    var server: WServer
    var client: WClient? = null

    var pServer: PolicyServer? = null

    init {
        if (usePolicyServer) {
            server = WServer(ip, port, this, { startPolicyServer() })
            server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))

            // PolicyServerを起動する
            pServer = PolicyServer {
                server.start()
            }
        }
        else {
            server = WServer(ip, port, this)
            server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))
            server.start()
        }
    }

    // サーバーの接続が切断された時、PolicyServerに切り替える。
    private fun startPolicyServer() {
        // HAX: BugFix
        timer(initialDelay = 200, period = Long.MAX_VALUE) {
            this.cancel()
            server.stop()

            // PolicyServerを起動する
            pServer = PolicyServer {
                server = WServer(ip, port, this@WebSocketProxy, { startPolicyServer() })
                server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))
                server.start()
            }
        }
    }

    fun stop() {
        client?.close()
        server.stop()
    }

    fun send(packet: Packet) {
        val data = packet.write()
        println("[WS SEND (FORCED)] ${data?.array()?.size} bytes")
        println(data?.array()?.toHexString())
        client?.send(data)
    }

    fun receive(packet: Packet) {
        val data = packet.write()
        println("[WS RECV (FORCED)] ${data?.array()?.size} bytes")
        println(data?.array()?.toHexString())
        server.broadcast(data?.array())
    }
}