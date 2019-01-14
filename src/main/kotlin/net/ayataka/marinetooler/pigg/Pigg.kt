@file:Suppress("MemberVisibilityCanBePrivate")

package net.ayataka.marinetooler.pigg

import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.listener.InfoPacketListener
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.getSSLContextFromPFXFile
import net.ayataka.marinetooler.utils.toHexString
import java.nio.ByteBuffer

object Pigg {
    const val PROXY_IP = "127.0.0.1"
    const val INFO_SERVER_PORT = 443
    const val INFO_SERVER_URI = "wss://27.133.213.64:443/command"
    val protocol = Protocol()
    val CERTIFICATE = getSSLContextFromPFXFile("pigg.pfx", "pigg.jks", "nopass")
    val proxies = hashMapOf<ServerType, WebSocketProxy>()

    init {
        // Start info server proxy
        proxies[ServerType.INFO] = WebSocketProxy(PROXY_IP, INFO_SERVER_PORT, INFO_SERVER_URI, InfoPacketListener(), CERTIFICATE, true)
    }

    fun send(packet: Packet) {
        packet.write(protocol.cipherKey[packet.server])?.let {
            proxies[packet.server]?.send(it)
        }
    }

    fun receive(packet: Packet) {
        packet.write(protocol.cipherKey[packet.server])?.let {
            proxies[packet.server]?.receive(it)
        }
    }

    fun sendChunk(packets: Collection<Packet>) {
        ServerType.values().forEach { type ->
            val key = protocol.cipherKey[type] ?: return@forEach
            val toSend = packets
                    .filter { it.server == type }
                    .mapNotNull { it.write(key)?.array()?.toHexString() }
                    .joinToString(separator = " ")
                    .fromHexToBytes()

            proxies[type]?.send(ByteBuffer.wrap(toSend))
        }
    }
}