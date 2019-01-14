package net.ayataka.marinetooler.proxy.http

import net.ayataka.marinetooler.utils.AlwaysConnectionReuseStrategy
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.warn
import org.apache.http.impl.DefaultBHttpServerConnection
import org.apache.http.impl.DefaultHttpResponseFactory
import org.apache.http.protocol.*
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket

class HttpProxy(port: Int, deamon: Boolean) : Thread() {
    private val serverSocket: ServerSocket = ServerSocket(port, 50, InetAddress.getLoopbackAddress())
    private val httpService: HttpService

    init {
        isDaemon = deamon

        val httpProcessor = ImmutableHttpProcessor(RequestContent(), RequestTargetHost(), RequestConnControl(), RequestUserAgent(), RequestExpectContinue())

        val requestHandlerRegistry = UriHttpRequestHandlerMapper()
        requestHandlerRegistry.register("*", ProxyHandler())

        httpService = HttpService(httpProcessor, AlwaysConnectionReuseStrategy(), DefaultHttpResponseFactory(), requestHandlerRegistry)
    }

    override fun run() {
        info("[HTTP] Listening on port ${serverSocket.localPort}")

        // Infinite loop until this thread is canceled.
        while (!Thread.interrupted()) {
            try {
                val socket = serverSocket.accept()
                val connection = DefaultBHttpServerConnection(8 * 1024)

                //info("Incoming connection from ${socket.inetAddress}")
                connection.bind(socket)

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