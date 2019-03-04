package net.ayataka.marinetooler.icearea.area

/*
import net.ayataka.marinetooler.icearea.Connection
import net.ayataka.marinetooler.icearea.IceAreaServer
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineFurniture
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceFurniture
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.MoveEndPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket
import net.ayataka.marinetooler.pigg.network.packet.send.PlaceFurniturePacket

class IceArea(val iceAreaData: IceAreaData) {
    private val avatars = mutableMapOf<String, PlaceAvatar>()

    fun onLeave(connection: Connection) {
        avatars.remove(connection.userCode)

        sendPacketToAll(
                LeaveUserPacket().apply {
                    userCode = connection.userCode
                    areaCode = iceAreaData.areaCode
                },
                connection.userCode
        )
    }

    fun onJoin(connection: Connection) {
        avatars[connection.userCode] = PlaceAvatar().apply { characterId = connection.userCode }

        sendPacketToAll(
                AppearUserPacket().apply {
                    areaCode = iceAreaData.areaCode
                    avatarData = connection.avatarDefinition!!.data
                },
                connection.userCode
        )

        sendPacket(connection, EnterAreaResult().apply {
            areaData = iceAreaData.areaData
            placeFurnitures = iceAreaData.placeFurnitures
            defineFurnitures = iceAreaData.defineFurnitures

            placeAvatars = avatars.keys.map { avatars[it]!! }.toMutableList()
            defineAvatars = avatars.keys.map { IceAreaServer.getConnection(it)!!.avatarDefinition }.toMutableList()

            println("DefineAvatars:")
            defineAvatars.forEach { println(it) }

            println("PlaceAvatars:")
            placeAvatars.forEach { println(it) }

            // Allow op users to modify the area
            if (iceAreaData.opUsers.contains(connection.userCode)) {
                //meta = 1
            }
            meta = 1
        })
    }

    fun onMove(connection: Connection, packet: MovePacket) {
        sendPacketToAll(MoveResultPacket().apply {
            usercode = connection.userCode
            x = packet.x
            y = packet.y
            z = packet.z
        })
    }

    fun onMoveEnd(connection: Connection, packet: MoveEndPacket) {
        avatars[connection.userCode]?.run {
            x = packet.x
            y = packet.y
            z = packet.z
            direction = packet.direction
        }

        sendPacketToAll(MoveEndResultPacket().apply {
            usercode = connection.userCode
            direction = packet.direction
            x = packet.x
            y = packet.y
            z = packet.z
        })
    }

    fun onPlaceFurniture(connection: Connection, packet: PlaceFurniturePacket) {
        val result = PlaceFurnitureResult().apply {
            placeFurniture = PlaceFurniture().apply {
                sequence = iceAreaData.placeFurnitures.size + 1
                x = packet.x.toShort()
                y = packet.y.toShort()
                z = packet.z.toShort()
                direction = packet.direction
            }

            defineFurniture = DefineFurniture().apply {
                characterId = packet.code
                category = "furniture"
            }

            iceAreaData.placeFurnitures.add(placeFurniture)
            iceAreaData.defineFurnitures.add(defineFurniture)
        }

        sendPacketToAll(result)
    }

    private fun sendPacketToAll(packet: Packet, sender: String? = null) {
        avatars.keys
                .mapNotNull { IceAreaServer.getConnection(it) }
                .filter { it.userCode != sender }
                .forEach { IceAreaServer.sendPacket(it, packet) }
    }

    private fun sendPacket(connection: Connection, packet: Packet) {
        IceAreaServer.sendPacket(connection, packet)
    }
}
*/