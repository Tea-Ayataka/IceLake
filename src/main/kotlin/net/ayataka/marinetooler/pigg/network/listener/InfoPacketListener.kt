package net.ayataka.marinetooler.pigg.network.listener

import com.darkmagician6.eventapi.EventManager
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.proxy.websocket.IPacketListener
import net.ayataka.marinetooler.proxy.websocket.WebSocketProxy
import net.ayataka.marinetooler.utils.dump
import java.nio.ByteBuffer

class InfoPacketListener : IPacketListener {
    // Raw
    override fun send(buffer: ByteBuffer): ByteBuffer? {
        Protocol.convert(buffer, ServerType.INFO)?.let {
            val packet = this.onSend(it)
            return if (packet.canceled) null else packet.write() ?: buffer
        }

        return buffer
    }

    override fun receive(buffer: ByteBuffer): ByteBuffer? {
        Protocol.convert(buffer, ServerType.INFO)?.let {
            val packet = this.onReceive(it)
            return if (packet.canceled) null else packet.write() ?: buffer
        }

        return buffer
    }

    private fun onSend(packet: Packet): Packet {
        val event = SendPacketEvent(packet)
        EventManager.call(event)
        return event.packet
    }

    private fun onReceive(packet: Packet): Packet {
        // restart chat proxy
        if (packet is GetAreaResultPacket) {
            dump(" CHAT SERVER URI CHANGED FROM ${packet.chatServerUri}")

            // Set user code
            if (CurrentUser.usercode == null) {
                CurrentUser.usercode = packet.areacode
            }

            CurrentUser.areaData = BaseAreaData()

            Pigg.proxies[ServerType.CHAT]?.stop()
            Pigg.proxies[ServerType.CHAT] = WebSocketProxy(Pigg.PROXY_IP, Pigg.CHAT_SERVER_PORT, packet.chatServerUri, ChatPacketListener(), Pigg.CERTIFICATE)

            packet.chatServerUri = "wss://${Pigg.PROXY_IP}:${Pigg.CHAT_SERVER_PORT}/command"

            dump(" TO ${packet.chatServerUri}")

            return packet
        }

        val event = RecvPacketEvent(packet)
        EventManager.call(event)
        return event.packet
    }
}