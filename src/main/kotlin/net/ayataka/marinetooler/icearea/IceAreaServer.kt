package net.ayataka.marinetooler.icearea

import net.ayataka.marinetooler.IceLake
import net.ayataka.marinetooler.icearea.area.IceArea
import net.ayataka.marinetooler.icearea.area.IceAreaData
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.pigg.network.packet.recv.ErrorPacket
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginChatResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

object IceAreaServer {
    val cipherKey = "00 00 00 00 00 00 00 00".fromHexToBytes()
    val protocol = Protocol()

    val connections = mutableListOf<Connection>()

    val areaList = listOf(
            IceArea(IceAreaData("test_001", "テストエリア001"))
    )

    val server = object : WebSocketServer(InetSocketAddress("localhost", 11451)) {
        init {
            protocol.cipherKey[ServerType.CHAT] = cipherKey

            /* timer(period = 120000) {
                 file.writeText(gson.toJson(iceAreaData))
             }*/
        }

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            conn.send("01 f0 00 00 00 00 00 00 00 00".fromHexToBytes())
        }

        override fun onClose(socket: WebSocket, code: Int, reason: String, remote: Boolean) {
            val connection = connections.find { it.socket == socket } ?: return
            val area = areaList.find { it.iceAreaData.areaCode == connection.areaCode } ?: return

            area.onLeave(connection)
            connections.remove(connection)
        }

        override fun onMessage(socket: WebSocket, buffer: ByteBuffer) {
            //Ping pong
            if (buffer.array().toHexString() == "01 ff 00 00 00 00 00 00 00 00 00 00") {
                socket.send("01 ff".fromHexToBytes())
                return
            }

            val packet = protocol.convert(buffer, ServerType.CHAT, PacketDirection.RECEIVE) ?: return
            val connection = connections.find { it.socket == socket }

            if (packet is LoginChatPacket) {
                info("Connected from ${packet.userCode}")
                socket.send(LoginChatResultPacket())
                connections.add(Connection(socket, packet.userCode, null, DefineAvatar(AvatarData().apply { readFrom(ByteBuilder(packet.data!!)) })))
            }

            if (packet is EnterRoomPacket) {
                val area = areaList.find { it.iceAreaData.areaCode == packet.code }
                if (area == null) {
                    socket.send(ErrorPacket().apply {
                        message = "Area not found"
                    })
                    return
                }

                connection!!.areaCode = area.iceAreaData.areaCode
                area.onJoin(connection)
            }

            // Handle area packets
            connection!!
            val area = areaList.find { it.iceAreaData.areaCode == connection.areaCode } ?: return

            if (packet is MovePacket) {
                area.onMove(connection, packet)
            }

            if (packet is MoveEndPacket) {
                area.onMoveEnd(connection, packet)
            }

            if (packet is TalkPacket) {

            }

            if (packet is PlaceFurniturePacket) {
                area.onPlaceFurniture(connection, packet)
            }
        }

        private fun WebSocket.send(packet: Packet) {
            send(packet.write(cipherKey)!!)
        }

        override fun onStart() {}
        override fun onError(conn: WebSocket, ex: Exception) {}
        override fun onMessage(conn: WebSocket, message: String) {}
    }

    init {
        server.start()
        info("IceArea Server Started")
    }

    fun getConnection(usercode: String): Connection? {
        return connections.find { it.userCode == usercode }
    }

    fun sendPacket(connection: Connection, packet: Packet) {
        val data = packet.write(cipherKey)!!
        println("[WS SEND] ${ChatPacketID.values().first { it.id == packet.packetId }.name} (${data.array().size} bytes)")
        println(data.array().toHexString())
        connection.socket.send(data)
    }
}

fun main(args: Array<String>) {
    IceLake()
    IceAreaServer
}