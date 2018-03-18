package net.ayataka.marinetooler.proxy.http

import net.ayataka.marinetooler.utils.AlwaysConnectionReuseStrategy
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.warn
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.impl.DefaultHttpServerConnection
import org.apache.http.params.CoreConnectionPNames
import org.apache.http.params.CoreProtocolPNames
import org.apache.http.params.HttpParams
import org.apache.http.params.SyncBasicHttpParams
import org.apache.http.protocol.*
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket

class HttpProxy(port: kotlin.Int, deamon: Boolean) : Thread() {
    private val serverSocket: ServerSocket = ServerSocket(port, 50, InetAddress.getLoopbackAddress())
    private val params: HttpParams = SyncBasicHttpParams()
    private val httpService: HttpService

    init {
        isDaemon = deamon
        params
                .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60 * 1000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1")

        val httpProcessor = ImmutableHttpProcessor(RequestContent(), RequestTargetHost(), RequestConnControl(), RequestUserAgent(), RequestExpectContinue())

        val requestHandlerRegistry = HttpRequestHandlerRegistry()
        requestHandlerRegistry.register("*", ProxyHandler())

        httpService = HttpService(httpProcessor, AlwaysConnectionReuseStrategy(), DefaultHttpResponseFactory(), requestHandlerRegistry, params)
    }

    override fun run() {
        info("[HTTP] Listening on port ${serverSocket.localPort}")

        // Infinite loop until this thread is canceled.
        while (!Thread.interrupted()) {
            try {
                val socket = serverSocket.accept()
                val connection = DefaultHttpServerConnection()

                //info("Incoming connection from ${socket.inetAddress}")
                connection.bind(socket, params)

                val thread = ProxyThread(httpService, connection, socket)
                thread.isDaemon = true
                thread.start()

            } catch (ex: InterruptedException) {
                break
            } catch (ex: IOException) {
                warn("[HTTP] IO ERROR occurred during connection thread initialization!")
                ex.printStackTrace()
                break
            }
        }
    }
}