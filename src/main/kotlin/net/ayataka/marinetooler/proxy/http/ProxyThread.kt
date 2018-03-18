package net.ayataka.marinetooler.proxy.http

import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info
import org.apache.http.HttpServerConnection
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpService
import java.net.Socket

class ProxyThread(
        private val httpService: HttpService,
        private val connection: HttpServerConnection,
        private val socket: Socket
) : Thread() {
    override fun run() {
        // info("New Connection Thread")

        val context = BasicHttpContext()
        var sslMode = SSLMode.NONE

        // Default parameters. these params may be edited in ProxyHandler and handle them in this instance.
        context.setAttribute("ssl.mode", sslMode)
        context.setAttribute("ssl.host", "")
        context.setAttribute("ssl.port", -1)

        // TODO: Refactor
        try {
            // Handle request
            while (!Thread.interrupted() && connection.isOpen) {
                sslMode = context.getAttribute("ssl.mode") as SSLMode

                if (sslMode != SSLMode.NONE) {
                    dump("Enabling $sslMode tunneling mode...")
                    break
                } else {
                    httpService.handleRequest(connection, context)
                    sslMode = context.getAttribute("ssl.mode") as SSLMode

                    if (sslMode == SSLMode.NONE) {
                        connection.close()
                    }
                }
            }

            // Throw another handler if the connection become HTTPS or WSS
            if (sslMode != SSLMode.NONE) {
                val host = context.getAttribute("ssl.host") as String
                val port = context.getAttribute("ssl.port") as Int

                println("[$sslMode Connection] Host: $host, Port: $port, Protocol: $sslMode")

                // Packet Interceptor
                val remoteSocket = if (sslMode == SSLMode.WEBSOCK_SECURE) Socket("localhost", 443) else Socket(host, port)

                // Start TCP Relayer
                val sender = TCPRelayer(socket, remoteSocket)
                val reader = TCPRelayer(remoteSocket, socket)

                sender.isDaemon = true
                reader.isDaemon = true

                sender.start()
                reader.start()
            }
        } catch (ex: Exception) {
            //ex.printStackTrace()
        } finally {
            if (sslMode == SSLMode.NONE) {
                connection.shutdown()
            }
        }
    }
}