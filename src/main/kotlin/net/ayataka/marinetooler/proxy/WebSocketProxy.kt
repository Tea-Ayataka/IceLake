package net.ayataka.marinetooler.proxy

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import net.ayataka.marinetooler.proxy.proxy.WClient
import net.ayataka.marinetooler.proxy.proxy.WServer
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.start
import net.ayataka.marinetooler.utils.toHexString
import net.ayataka.marinetooler.utils.trace
import org.java_websocket.server.DefaultSSLWebSocketServerFactory
import java.nio.ByteBuffer
import javax.net.ssl.SSLContext
import kotlin.concurrent.timer

class WebSocketProxy(
        private val ip: String,
        val port: Int,
        val remoteUri: String,
        val ssl: SSLContext,
        val onSend: (ByteBuffer) -> ByteBuffer?,
        val onReceive: (ByteBuffer) -> ByteBuffer?,
        usePolicyServer: Boolean
) : CoroutineScope {
    @ObsoleteCoroutinesApi
    override val coroutineContext = newSingleThreadContext("WebSocket Proxy")

    var server: WServer
    var client: WClient? = null

    var pServer: PolicyServer? = null

    init {
        if (usePolicyServer) {
            server = WServer(ip, port, this) { startPolicyServer() }
            server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))

            // PolicyServerを起動する
            pServer = PolicyServer {
                server.start()
            }
        } else {
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
                server = WServer(ip, port, this@WebSocketProxy) { startPolicyServer() }
                server.setWebSocketFactory(DefaultSSLWebSocketServerFactory(ssl))
                server.start()
            }
        }
    }

    fun stop() {
        client?.close()

        server.connections().forEach { it.close(2000, "Disconnect") }
        //server.stop()
        info("Disconnected Clients")
    }

    fun send(data: ByteArray) = start {
        trace("[WS SEND (FORCED)] ${data.size} bytes")
        trace(data.toHexString())
        client?.send(data)
    }

    fun receive(data: ByteArray) = start {
        trace("[WS RECV (FORCED)] ${data.size} bytes")
        trace(data.toHexString())
        server.broadcast(data)
    }
}