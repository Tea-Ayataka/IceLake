@file:Suppress("MemberVisibilityCanBePrivate")

package net.ayataka.marinetooler.pigg

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.MoveEndResultPacket
import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.getSSLContextFromPFXFile
import net.ayataka.marinetooler.utils.getUnusedPort
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.math.Vec3i
import net.ayataka.marinetooler.utils.trace
import java.nio.ByteBuffer
import kotlin.collections.set

object PiggProxy {
    private const val LISTEN_HOST = "127.0.0.1"
    private const val INFO_SERVER_PORT = 443
    private const val INFO_SERVER_URI = "wss://27.133.213.64:443/command"

    private val protocol = Protocol()
    private val CERTIFICATE = getSSLContextFromPFXFile("pigg.pfx", "pigg.jks", "nopass")
    private val proxies = hashMapOf<ServerType, WebSocketProxy>()

    init {
        // Start info server proxy
        proxies[ServerType.INFO] = WebSocketProxy(LISTEN_HOST, INFO_SERVER_PORT, INFO_SERVER_URI, CERTIFICATE, ::onSendInfo, ::onReceiveInfo, true)
    }

    private fun onSendInfo(buffer: ByteBuffer): ByteBuffer? {
        val packet = protocol.convert(buffer, ServerType.INFO, PacketDirection.SEND) ?: return buffer

        val event = SendPacketEvent(packet)
        EventManager.fire(event)

        return if (event.canceled) null else packet.write(protocol.cipherKey[ServerType.INFO]) ?: buffer
    }

    private fun onReceiveInfo(buffer: ByteBuffer): ByteBuffer? {
        val packet = protocol.convert(buffer, ServerType.INFO, PacketDirection.RECEIVE) ?: return buffer

        // Modify the chat server url
        if (packet is GetAreaResultPacket) {
            CurrentUser.usercode = packet.userCode // Update user code
            CurrentUser.areaData = BaseAreaData() // Reset area data

            val port = getUnusedPort()

            proxies[ServerType.CHAT] = WebSocketProxy(PiggProxy.LISTEN_HOST, port, packet.chatServerUri, CERTIFICATE, ::onSendChat, ::onReceiveChat, false)
            packet.chatServerUri = "wss://${PiggProxy.LISTEN_HOST}:$port/command"

            trace("The chat server uri has modified to ${packet.chatServerUri}")
        }

        val event = ReceivePacketEvent(packet)
        EventManager.fire(event)

        return if (event.canceled) null else packet.write(protocol.cipherKey[ServerType.INFO]) ?: buffer
    }

    private fun onSendChat(buffer: ByteBuffer): ByteBuffer? {
        val packet = protocol.convert(buffer, ServerType.CHAT, PacketDirection.SEND) ?: return buffer

        val event = SendPacketEvent(packet)
        EventManager.fire(event)

        return if (event.canceled) null else packet.write(protocol.cipherKey[ServerType.CHAT]) ?: buffer
    }

    private fun onReceiveChat(buffer: ByteBuffer): ByteBuffer? {
        val packet = protocol.convert(buffer, ServerType.CHAT, PacketDirection.RECEIVE) ?: return buffer

        // Update location
        if (packet is MoveEndResultPacket && packet.usercode == CurrentUser.usercode) {
            CurrentUser.location = Vec3i(packet.x.toInt(), packet.y.toInt(), packet.z.toInt())
        }

        // Update area data
        if (packet is BaseAreaData) {
            CurrentUser.areaData = packet
            info("Entered to ${packet.areaData.categoryCode}.${packet.areaData.areaCode} with ${packet.defineAvatars.size} users")
        }

        val event = ReceivePacketEvent(packet)
        EventManager.fire(event)

        return if (event.canceled) null else packet.write(protocol.cipherKey[ServerType.CHAT]) ?: buffer
    }

    fun send(packet: Packet) {
        packet.write(protocol.cipherKey[packet.server])?.let {
            send(packet.server, it.array())
        }
    }

    fun receive(packet: Packet) {
        packet.write(protocol.cipherKey[packet.server])?.let {
            receive(packet.server, it.array())
        }
    }

    fun send(server: ServerType, data: ByteArray) {
        proxies[server]?.send(data)
    }

    fun receive(server: ServerType, data: ByteArray) {
        proxies[server]?.receive(data)
    }

    fun getCipherKey(server: ServerType) = protocol.cipherKey[server]
}