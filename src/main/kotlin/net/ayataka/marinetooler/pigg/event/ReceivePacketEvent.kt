package net.ayataka.marinetooler.pigg.event

import net.ayataka.eventapi.Event
import net.ayataka.marinetooler.pigg.network.packet.Packet

class ReceivePacketEvent(val packet: Packet) : Event() {
    var canceled: Boolean = false
}