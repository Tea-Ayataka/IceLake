package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.ReceivePacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.GetAreaPacket

object AreaEnterPrevent : Module() {
    var lastGetAreaPacket: GetAreaPacket? = null
    val protocol = Protocol()

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        if (event.packet is GetAreaPacket) {
            lastGetAreaPacket = event.packet as GetAreaPacket
        }
    }

    @EventListener
    fun onRecvPacket(event: ReceivePacketEvent) {
        val packet = event.packet

        if (packet is GetAreaResultPacket && lastGetAreaPacket != null) {
            event.canceled = true
            PiggProxy.receive(GetAreaResultPacket())

            // Simulate
            /*object : VClient(packet.chatServerUri) {
                var bettable = true
                var first = true
                var timer: Timer? = null
                var slotThread: Thread? = null

                override fun onMessage(message: ByteBuffer?) {
                    if (message == null) return

                    dump("[VWS RECV] ${message.array().toHexString()}")
                    dump("[VWS RECV] ${message.array().let { String(it) }}")

                    val packet = protocol.convert(message, ServerType.CHAT, PacketDirection.RECEIVE)

                    if (first) {
                        first = false
                        val loginChatPacket = LoginChatPacket().apply {
                            userCode = CurrentUser.usercode!!
                            secure = CurrentUser.secure!!
                            connectionId = protocol.connectionId[ServerType.CHAT]!!
                        }

                        dump("$loginChatPacket")

                        send(loginChatPacket.write(protocol.cipherKey[ServerType.CHAT])!!)

                        timer = timer(period = 15 * 1000) {
                            send("01 ff 00 00 00 00 00 00 00 00 00 00".fromHexToBytes())
                        }
                    }

                    if (packet is LoginChatResultPacket) {
                        val enterRoomPacket = EnterRoomPacket().apply {
                            category = lastGetAreaPacket!!.category
                            code = lastGetAreaPacket!!.code
                        }

                        send(enterRoomPacket.write(protocol.cipherKey[ServerType.CHAT])!!)
                    }

                    if (packet is EnterAreaResult || packet is EnterUserGardenResult || packet is EnterUserRoomResult) {
                        if (packet is EnterAreaResult && packet.areaData.areaCode.contains("baccarat")) {
                            info("Starting Bacara")

                            slotThread = Thread {
                                // GAME_JOIN
                                Thread.sleep(1000)
                                info("JOIN GAME")
                                send("00 10 00 00 00 00 08 00 00 00 00 13 00 00 01 2f 00 08 62 61 63 63 61 72 61 74 01 00 00 00 02".fromHexToBytes())
                                Thread.sleep(500)
                                send("00 10 00 00 00 00 08 10 00 00 00 0b 00 04 6a 6f 69 6e 01 00 00 03 e8".fromHexToBytes())

                                while (true) {
                                    while (!bettable) {
                                        Thread.sleep(100)
                                    }
                                    bettable = false
                                    info("baccarat!")
                                    send("00 10 00 00 00 00 08 10 00 00 00 0b 00 03 62 65 74 01 00 00 00 01 f4".fromHexToBytes())
                                    send("00 10 00 00 00 00 08 10 00 00 00 0b 00 03 62 65 74 01 00 00 00 01 f4".fromHexToBytes())
                                    send("00 10 00 00 00 00 08 10 00 00 00 0a 00 07 62 65 74 44 6f 6e 65 00".fromHexToBytes())
                                }
                            }
                            slotThread?.start()
                        }
                    }

                    if (message.array()!!.contentEquals("00 10 00 00 00 00 08 11 00 00 00 0c 00 08 73 74 61 72 74 42 65 74 00 01".fromHexToBytes())) {
                        bettable = true
                    }
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                    dump("VWS DISCONNECT")
                    timer?.cancel()
                    slotThread?.interrupt()
                }
            }.connect()*/
        }
    }
}