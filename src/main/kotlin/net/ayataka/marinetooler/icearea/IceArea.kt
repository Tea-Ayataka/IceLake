package net.ayataka.marinetooler.icearea

import net.ayataka.marinetooler.IceLake
import net.ayataka.marinetooler.pigg.network.PacketDirection
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.area.AreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.EnterUserRoomResult
import net.ayataka.marinetooler.pigg.network.packet.recv.LoginChatResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.EnterRoomPacket
import net.ayataka.marinetooler.pigg.network.packet.send.LoginChatPacket
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.info
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress
import java.nio.ByteBuffer

object IceArea {
    val server = object : WebSocketServer(InetSocketAddress("localhost", 11451)) {
        val cipherKey = "00 00 00 00 00 00 00 00".fromHexToBytes()
        val protocol = Protocol()
        val usercodes: MutableMap<WebSocket, String> = hashMapOf()

        init {
            protocol.cipherKey[ServerType.CHAT] = cipherKey
        }

        override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {
            conn.send("01 f0 00 00 00 00 00 00 00 00".fromHexToBytes())
        }

        override fun onMessage(socket: WebSocket, buffer: ByteBuffer) {
            val packet = protocol.convert(buffer, ServerType.CHAT, PacketDirection.RECEIVE) ?: return

            if (packet is LoginChatPacket) {
                info("Connected from ${packet.userCode}")
                usercodes[socket] = packet.userCode
                socket.send(LoginChatResultPacket())
            }

            if (packet is EnterRoomPacket) {
                info("EnterRoom!")
                socket.send(EnterUserRoomResult().apply {
                    areaData = AreaData().apply {
                        categoryCode = "user"
                        areaCode = "4d2b2e102ba85faf"
                        areaName = "うんちっち"
                        wallCode = "default_extr_brownhouse_1302"
                        windowCode = "fishing_clioneneon_highwindow"
                        floorCode = "navizouhouse_floor_1503_X38Y38"
                        sizeX = 15
                        sizeY = 5
                    }
                    data = "00 00 00 01 00 00 00 01 00 10 34 64 32 62 32 65 31 30 32 62 61 38 35 66 61 66 00 00 00 01 00 00 00 01 00 19 34 64 32 62 32 65 31 30 32 62 61 38 35 66 61 66 5f 67 61 72 64 65 6e 5f 31 00 00 00 00 00 06 e2 80 8c e2 80 8c 00 0b 69 33 66 72 39 31 6d 30 74 68 65 00 10 34 64 32 62 32 65 31 30 32 62 61 38 35 66 61 66 00 0a 39 34 31 36 32 38 33 33 35 32 03 00 00 00 00 01 01 01 00 01 01 00 00 01 01 01 01 01 00 01 01 00 03 3b 5f 3b 00 00 00 10 34 64 32 62 32 65 31 30 32 62 61 38 35 66 61 66 00 10 34 64 32 62 32 65 31 30 32 62 61 38 35 66 61 66 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00".fromHexToBytes()
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

fun main(args: Array<String>) {
    IceLake()
    IceArea
}