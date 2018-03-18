package net.ayataka.marinetooler.pigg.event

import net.ayataka.eventapi.Event
import net.ayataka.marinetooler.pigg.network.packet.Packet

class RecvPacketEvent(var packet: Packet) : Event()