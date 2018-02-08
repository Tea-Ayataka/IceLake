package net.ayataka.marinetooler.proxy.http

import net.ayataka.marinetooler.pigg.swf.SWFInjector
import net.ayataka.marinetooler.utils.*
import org.apache.http.HttpRequest
import org.apache.http.HttpResponse
import org.apache.http.HttpStatus
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.impl.DefaultHttpClientConnection
import org.apache.http.message.BasicHttpRequest
import org.apache.http.params.CoreConnectionPNames
import org.apache.http.params.CoreProtocolPNames
import org.apache.http.params.SyncBasicHttpParams
import org.apache.http.protocol.*
import org.apache.http.util.EntityUtils
import java.net.Socket
import java.net.URL

class ProxyHandler : HttpRequestHandler {
    private val httpProcessor = ImmutableHttpProcessor(ResponseDate(), ResponseServer(), ResponseContent(), ResponseConnControl())
    private val httpExecutor = HttpRequestExecutor()
    private val httpParams = SyncBasicHttpParams()

    init {
        this.httpParams
                .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 5000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER, "HttpComponents/1.1")
    }

    override fun handle(request: HttpRequest?, response: HttpResponse?, context: HttpContext?) {
        dump("[HTTP PROXY] ${request!!.requestLine!!.uri}")

        val requestMethod = request.requestLine.method

        // SSL (Forward-Everything mode)
        if (requestMethod == "CONNECT") {
            dump("CONNECT method detected. Switching connection to secure SSL")

            // Send 'Connection Established' message to the browser
            response!!.setStatusCode(HttpStatus.SC_OK)
            response.setReasonPhrase("Connection established")


            // Get hostname and port from the header
            val hostHeader = request.getFirstHeader("Host").value

            var port = 443
            val hostName = if (hostHeader.contains(":")) {
                port = hostHeader.split(":").last().toInt()
                hostHeader.split(":").first()
            } else {
                hostHeader
            }

            // Set internal flags
            context!!.setAttribute("ssl.host", hostName)
            context.setAttribute("ssl.port", port)
            context.setAttribute("ssl.mode", if (hostName == "pigg.ameba.jp") SSLMode.HTTPS else SSLMode.HTTPS)

            dump("SSL CONNECTION TO $hostName:$port")
            return
        }

        // Non-SSL

        if (!validateUrl(request.requestLine.uri)) {
            warn("Invalid URL. [${request.requestLine.uri}]")
            return
        }

        val url = URL(request.requestLine.uri)
        val host = url.host
        val port = if (url.port == -1) 80 else url.port
        val path = url.file

        // Setup outgoing connection
        val outSocket = Socket(host, port)
        val outConnection = DefaultHttpClientConnection()

        outConnection.bind(outSocket, this.httpParams)

        //info("Outgoing connection to ${outSocket.inetAddress}")

        request.removeProxyHeaders()

        // Send http request to remote server
        val remoteRequest = BasicHttpRequest(request.requestLine.method, path, request.requestLine.protocolVersion)
        remoteRequest.setHeaders(request.allHeaders)

        val remoteResponse = this.httpExecutor.execute(remoteRequest, outConnection, context)
        val responseEntity = remoteResponse.entity

        // Grab data
        //info("Remote server responded with ${remoteResponse.statusLine}")
        var responseData = if (responseEntity != null) EntityUtils.toByteArray(responseEntity) else ByteArray(0)

        // Dispose
        EntityUtils.consume(responseEntity)

        // Sniff
        //info("PATH IS " + path)
        if (path.startsWith("/swf/pigg.swf")) {
            info("[HTTP PROXY] %%%%%%%%%%% INTERCEPTED %%%%%%%%%%%%%%")
            info("[HTTP PROXY] ${request.requestLine!!.uri}")

            val downloaded = URL("http://pigg.ameba.jp$path").openConnection().getInputStream().readBytes()
            info("Injecting bytecode to SWF...")
            responseData = SWFInjector.inject(downloaded)
            info("Injected.")
            remoteResponse.setStatusLine(remoteResponse.protocolVersion, 200)
        }

        //responseData = "<h2> Intercepted!!!! FUCK DAMEGA PIGG!!! </h2>".toByteArray()

        this.httpExecutor.postProcess(response, this.httpProcessor, context)

        remoteResponse.removeProxyHeaders()

        // Copy
        response!!.statusLine = remoteResponse.statusLine
        response.setHeaders(remoteResponse.allHeaders)
        response.entity = ByteArrayEntity(responseData)

        //println("<< Response: ${response.statusLine}")

        try {
            // Ignore keep-alive
            outSocket.close()
        } catch (ex: Exception) {
        }
    }
}
