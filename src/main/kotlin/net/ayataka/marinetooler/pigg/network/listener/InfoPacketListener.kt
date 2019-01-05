package net.ayataka.marinetooler.pigg.network.listener

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.GetPetProfile
import net.ayataka.marinetooler.proxy.IPacketListener
import net.ayataka.marinetooler.proxy.WebSocketProxy
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.getUnusedPort
import net.ayataka.marinetooler.utils.toHexString
import java.nio.ByteBuffer

class InfoPacketListener : IPacketListener {
    // Raw
    override fun send(buffer: ByteBuffer): ByteBuffer? {
        Pigg.protocol.convert(buffer, ServerType.INFO, PacketDirection.SEND)?.let {
            val packet = onSend(it)
            return if (packet.canceled) null else packet.write(Pigg.protocol.cipherKey[packet.server]) ?: buffer
        }

        return buffer
    }

    override fun receive(buffer: ByteBuffer): ByteBuffer? {
        Pigg.protocol.convert(buffer, ServerType.INFO, PacketDirection.RECEIVE)?.let {
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

    fun onReceive(packet: Packet): Packet {
        EventManager.fire(RecvPacketEvent(packet))

        // restart chat proxy
        if (!packet.canceled && packet is GetAreaResultPacket) {
            dump(" CHAT SERVER URI CHANGED FROM ${packet.chatServerUri}")

            // Set user code
            if (CurrentUser.usercode == null) {
                CurrentUser.usercode = packet.userCode
            }

            CurrentUser.areaData = BaseAreaData()
            val port = getUnusedPort()
            Pigg.proxies[ServerType.CHAT] = WebSocketProxy(Pigg.PROXY_IP, port, packet.chatServerUri, ChatPacketListener(), Pigg.CERTIFICATE)

            packet.chatServerUri = "wss://${Pigg.PROXY_IP}:$port/command"

            dump(" TO ${packet.chatServerUri}")
        }

        if (packet is LoginResultPacket) {
            CurrentUser.secure = packet.secure
            dump("Secure code: ${packet.secure.toHexString()}")
        }



        return packet
    }
}