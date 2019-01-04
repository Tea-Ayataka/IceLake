package net.ayataka.marinetooler.pigg.network.listener

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.GetPetProfile
import net.ayataka.marinetooler.proxy.websocket.IPacketListener
import net.ayataka.marinetooler.proxy.websocket.WebSocketProxy
import net.ayataka.marinetooler.utils.dump
import java.nio.ByteBuffer

class InfoPacketListener : IPacketListener {
    // Raw
    override fun send(buffer: ByteBuffer): ByteBuffer? {
        Pigg.protocol.convert(buffer, ServerType.INFO)?.let {
            val packet = onSend(it)
            return if (packet.canceled) null else packet.write(Pigg.protocol.cipherKey[packet.server]) ?: buffer
        }

        return buffer
    }

    override fun receive(buffer: ByteBuffer): ByteBuffer? {
        Pigg.protocol.convert(buffer, ServerType.INFO)?.let {
            val packet = onReceive(it)
            return if (packet.canceled) null else packet.write(Pigg.protocol.cipherKey[packet.server]) ?: buffer
        }

        return buffer
    }

    private fun onSend(packet: Packet): Packet {
        if(packet is GetPetProfile) {
            CurrentUser.selectedPetId = packet.petId
        }

        val event = SendPacketEvent(packet)
        EventManager.fire(event)
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
        EventManager.fire(event)
        return event.packet
    }
}