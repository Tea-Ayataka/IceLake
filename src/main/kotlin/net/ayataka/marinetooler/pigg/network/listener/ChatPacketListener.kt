package net.ayataka.marinetooler.pigg.network.listener

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.proxy.websocket.IPacketListener
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.math.Vec3i
import java.nio.ByteBuffer

class ChatPacketListener : IPacketListener {
    // Raw
    override fun send(buffer: ByteBuffer): ByteBuffer? {
        Protocol.convert(buffer, ServerType.CHAT)?.let {
            val packet = onSend(it)
            return if (packet.canceled) null else packet.write() ?: buffer
        }
        return buffer
    }

    override fun receive(buffer: ByteBuffer): ByteBuffer? {
        Protocol.convert(buffer, ServerType.CHAT)?.let {
            val packet = onReceive(it)
            return if (packet.canceled) null else packet.write() ?: buffer
        }
        return buffer
    }

    private fun onSend(packet: Packet): Packet {
        val event = SendPacketEvent(packet)
        EventManager.fire(event)

        // Set location after handling events
        if (packet is MoveEndPacket) {
            CurrentUser.location = Vec3i(packet.x.toInt(), packet.y.toInt(), packet.z.toInt())
        }

        return event.packet
    }

    private fun onReceive(packet: Packet): Packet {
        // Set area data
        if (packet is BaseAreaData) {
            CurrentUser.areaData = packet

            info("JOINED TO ${packet.areaData.categoryCode}.${packet.areaData.areaCode} with ${packet.users.size} users.")
        }

        val event = RecvPacketEvent(packet)
        EventManager.fire(event)
        return event.packet
    }
}