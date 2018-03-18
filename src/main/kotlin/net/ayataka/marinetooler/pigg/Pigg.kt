@file:Suppress("MemberVisibilityCanBePrivate")

package net.ayataka.marinetooler.pigg

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.listener.InfoPacketListener
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.proxy.websocket.PolicyServer
import net.ayataka.marinetooler.proxy.websocket.WebSocketProxy
import net.ayataka.marinetooler.utils.getSSLContextFromPFXFile

object Pigg {
    const val PROXY_IP = "127.0.0.1"
    const val INFO_SERVER_PORT = 443
    const val CHAT_SERVER_PORT = 8443
    const val INFO_SERVER_URI = "wss://27.133.213.64:443/command"
    val CERTIFICATE = getSSLContextFromPFXFile("pigg.pfx", "pigg.jks", "nopass")

    val proxies = hashMapOf<ServerType, WebSocketProxy>()

    init {
        PolicyServer().start()
        // Start info server proxy
        proxies[ServerType.INFO] = WebSocketProxy(PROXY_IP, INFO_SERVER_PORT, INFO_SERVER_URI, InfoPacketListener(), CERTIFICATE)
    }

    fun send(packet: Packet) {
        proxies[packet.server]?.send(packet)
    }

    fun receive(packet: Packet) {
        proxies[packet.server]?.receive(packet)
    }
}