package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket
import net.ayataka.marinetooler.pigg.network.packet.send.PetMove
import net.ayataka.marinetooler.pigg.network.packet.send.PetMoveEnd
import net.ayataka.marinetooler.pigg.network.packet.send.SetPetProfile

object HackPet : Module() {
    private var isTeleportMode = false

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        val petId = CurrentUser.selectedPetId

        if (packet is MovePacket && petId != null) {
            event.canceled = true

            if (isTeleportMode) {
                teleport(petId, packet.x, packet.y, packet.z, 0)
            } else {
                move(petId, packet.x, packet.y, packet.z)
            }
        } else if (packet is SetPetProfile) {
            isTeleportMode = !isTeleportMode

            event.canceled = true
        }

    }

    fun move(petId: Int, x: Short, y: Short, z: Short) {
        val movePacket = PetMove().apply {
            this.petId = petId

            this.x = x
            this.y = y
            this.z = z
        }

        PiggProxy.send(movePacket)
    }

    fun teleport(petId: Int, x: Short, y: Short, z: Short, direction: Byte) {
        val packet = PetMoveEnd().apply {
            this.petId = petId

            this.x = x
            this.y = y
            this.z = z

            this.dir = direction
        }

        PiggProxy.send(packet)
    }
}