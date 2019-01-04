package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.MoveResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.runLater

object HackPet : Module() {
    private var teleportMode = false

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        val petId = CurrentUser.selectedPetId

        if(packet is MovePacket && petId != null){
            packet.canceled = true

            if(teleportMode) {
                teleport(petId, packet.x, packet.y, packet.z,0)
            }
            else{
                move(petId, packet.x, packet.y, packet.z)
            }
        }
        else if(packet is SetPetProfile){
            teleportMode = !teleportMode

            packet.canceled = true
        }

    }

    fun move(petId: Int, x: Short, y: Short, z: Short){
        val movePacket = PetMove().apply {
            this.petId = petId

            this.x = x
            this.y = y
            this.z = z
        }

        Pigg.send(movePacket)
    }

    fun teleport(petId: Int, x: Short, y: Short, z: Short, direction: Byte){
        val packet = PetMoveEnd().apply {
            this.petId = petId

            this.x = x
            this.y = y
            this.z = z


        }

        Pigg.send(packet)
    }
}