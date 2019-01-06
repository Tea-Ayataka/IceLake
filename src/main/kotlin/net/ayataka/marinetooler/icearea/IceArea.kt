package net.ayataka.marinetooler.icearea

import com.google.gson.Gson
import com.google.gson.JsonParser
import net.ayataka.marinetooler.IceLake
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefinePet
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlaceAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.place.PlacePet
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.recv.*
import net.ayataka.marinetooler.pigg.network.packet.send.EnterRoomPacket
import net.ayataka.marinetooler.pigg.network.packet.send.IceAreaPacket
import net.ayataka.marinetooler.pigg.network.packet.send.LoginChatPacket
import net.ayataka.marinetooler.pigg.network.packet.send.MovePacket
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

object IceArea {
    private val gson = Gson()
    private val file = File("icearea").apply {
        if (!exists()) {
            createNewFile()
        }
    }

    val iceAreaData = gson.fromJson(file.readText(), IceAreaData::class.java) ?: IceAreaData()

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
            }

            if (packet is EnterRoomPacket) {
                info("EnterRoom!")
                socket.send(EnterAreaResult().apply {
                    areaData = AreaData().apply {
                        categoryCode = "club"
                        areaCode = "114514"
                        areaName = iceAreaData.areaData["areaName"]!!
                        wallCode = iceAreaData.areaData["wallCode"]!!
                        windowCode = iceAreaData.areaData["windowCode"]!!
                        floorCode = iceAreaData.areaData["floorCode"]!!
                        sizeX = iceAreaData.areaData["sizeX"]!!.toShort()
                        sizeY = iceAreaData.areaData["sizeY"]!!.toShort()
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

                    data = ByteBuilder()
                            .writeByte(0)
                            .writeInt(0)
                            .writeBoolean(false)
                            .build().array()
                })
            }

            if(packet is MovePacket){
                val defineAvatar = avatars.keys.find { it.data.userCode == usercodes[socket] }

                avatars[defineAvatar]?.apply {
                    x = packet.x
                    y = packet.y
                    z = packet.z
                }

                socket.send(MoveResultPacket().apply {
                    usercode = usercodes[socket]!!

                    x = packet.x
                    y = packet.y
                    z = packet.z
                })
            }
        }

        private fun WebSocket.send(packet: Packet) {
            send(packet.write(cipherKey)!!)
        }

        override fun onStart() {}
        override fun onError(conn: WebSocket, ex: Exception) {}
        override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {}
        override fun onMessage(conn: WebSocket, message: String) {}
    }

    init {
        server.start()
        info("IceArea Server Started")
    }
}

var roomName = "・。・？"

fun main(args: Array<String>) {
    args.getOrNull(1)?.let { roomName = it }

    IceLake()
    IceArea
}