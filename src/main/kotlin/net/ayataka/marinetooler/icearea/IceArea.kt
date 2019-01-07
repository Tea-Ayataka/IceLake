package net.ayataka.marinetooler.icearea

import com.google.gson.Gson
import net.ayataka.marinetooler.IceLake
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaOwnerData
import net.ayataka.marinetooler.pigg.network.packet.data.area.Expansion
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceAvatar
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.io.File
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import kotlin.concurrent.timer
import kotlin.random.Random

object IceArea {
    private val gson = Gson()
    private val file = File("icearea").apply {
        if (!exists()) {
            createNewFile()
        }
    }

    val iceAreaData =  IceAreaData().apply {
        areaData = AreaData().apply {
            categoryCode = "user"
            areaCode = "icelake_area_001"
            areaName = "IceAreaのお部屋"

            sizeY = 16
            sizeX = 16
            wallCode = "small_basic_wall"

            opUsers.add("4d2b2e102ba85faf")
        }
    }

    val server = object : WebSocketServer(InetSocketAddress("localhost", 11451)) {
        val cipherKey = "00 00 00 00 00 00 00 00".fromHexToBytes()
        val protocol = Protocol()
        val usercodes: MutableMap<WebSocket, String> = hashMapOf()

        val avatars = mutableMapOf<DefineAvatar, PlaceAvatar?>()

        init {
            protocol.cipherKey[ServerType.CHAT] = cipherKey

            timer(period = 120000){
                file.writeText(gson.toJson(iceAreaData))
            }
        }

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            conn.send("01 f0 00 00 00 00 00 00 00 00".fromHexToBytes())
        }

        override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {
            val defineAvatar = getDefineAvatar(usercodes[conn]!!)

            avatars.remove(defineAvatar)
            usercodes.remove(conn)

            usercodes.forEach {
                it.key.send(LeaveUser().apply {
                    areaCode = "icelake_area_001"
                    userCode = it.value
                })
            }
        }


        override fun onMessage(socket: WebSocket, buffer: ByteBuffer) {
            //Ping pong
            if(buffer.array().toHexString() == "01 ff 00 00 00 00 00 00 00 00 00 00"){
                socket.send("01 ff".fromHexToBytes())

                return
            }

            val packet = protocol.convert(buffer, ServerType.CHAT, PacketDirection.RECEIVE) ?: return

            if (packet is LoginChatPacket) {
                info("Connected from ${packet.userCode}")
                usercodes[socket] = packet.userCode
                socket.send(LoginChatResultPacket())
                socket.send(AlertResultPacket().apply { message = "810" })
            }

            if(packet is IceAreaPacket){
                val defineAvatar = DefineAvatar().apply {
                    load(packet.avatarData)

                    part = PartData(false).apply {
                        height = 96
                    }
                }
                avatars[defineAvatar] = null

                usercodes.filterValues { it != packet.avatarData.userCode }.forEach {
                    it.key.send(AppearUserPacket().apply {
                        areaCode = "icelake_area_001"
                        avatarData = defineAvatar.data
                    })
                }
            }

            if (packet is EnterRoomPacket) {
                info("EnterRoom!")
                socket.send(EnterUserRoomResult().apply {
                    areaData = iceAreaData.areaData

                    if(iceAreaData.opUsers.contains(usercodes[socket])) {
                        meta = 1
                    }

                    avatars.forEach {
                        defineAvatars.add(it.key)

                        val placeAvatar = PlaceAvatar().apply {
                            characterId = it.key.characterId

                            x = areaData.sizeX
                        }

                        avatars[it.key] = placeAvatar

                        placeAvatars.add(placeAvatar)
                    }

                    placeFurnitures = iceAreaData.placeFurnitures
                    defineFurnitures = iceAreaData.defineFurnitures

                    expansions.add(Expansion().apply {
                        areaCode = "icelake_area_001"
                        index = 1
                    })

                    ownerData = AreaOwnerData().apply {
                        userCode = iceAreaData.opUsers.random()
                        numFootPrintToday = Random.nextInt(5000)
                    }
                })
            }

            if(packet is MovePacket){
                val packetToSend = MoveResultPacket().apply {
                    usercode = usercodes[socket]!!

                    x = packet.x
                    y = packet.y
                    z = packet.z
                }

                usercodes.keys.forEach { it.send(packetToSend) }
            }

            if(packet is MoveEndPacket){
                val defineAvatar = getDefineAvatar(usercodes[socket]!!)

                avatars[defineAvatar]?.apply {
                    x = packet.x
                    y = packet.y
                    z = packet.z

                    direction = packet.dir
                }

                val toSend = MoveEndResultPacket().apply {
                    usercode = usercodes[socket]!!

                    x = packet.x
                    y = packet.y
                    z = packet.z

                    dir = packet.dir
                }

                println("MoveEndPacket: $packet, MoveEndResultPacket: $toSend")

                usercodes.keys.forEach { it.send(toSend) }
            }

            if(packet is TalkPacket){
                val avatarData = getDefineAvatar(usercodes[socket]!!)?.data!!

                val toSend = TalkResultPacket().apply {
                    usercode = avatarData.userCode
                    message = packet.text
                    ballonColor = packet.balloonColor
                    color = packet.color
                    nickName = avatarData.nickName
                    amebaId = avatarData.amebaId
                    roomCode = "icelake_area_001"
                }

                usercodes.keys.forEach { it.send(toSend) }
            }
        }

        private fun WebSocket.send(packet: Packet) {
            send(packet.write(cipherKey)!!)
        }

        private fun getDefineAvatar(userCode: String): DefineAvatar? = avatars.keys.find { it.data.userCode == userCode }

        override fun onStart() {}
        override fun onError(conn: WebSocket, ex: Exception) {}
        override fun onMessage(conn: WebSocket, message: String) {}
    }

    init {
        server.start()
        info("IceArea Server Started")
    }
}

fun main(args: Array<String>) {
    IceLake()
    IceArea
}