package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.RecvPacketEvent
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.recv.AreaGamePlayResult
import net.ayataka.marinetooler.pigg.network.packet.send.AreaGameJoinPacket
import net.ayataka.marinetooler.pigg.network.packet.send.AreaGameLeavePacket
import net.ayataka.marinetooler.pigg.network.packet.send.AreaGamePlayPacket
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.random.Random

object FishMacro : Module() {
    @Volatile
    var kita = false

    @Volatile
    var ready = false

    @Volatile
    var task: Thread? = null

    @Volatile
    var lastFish = 0L

    fun start() {
        if (!ready) {
            return
        }

        kita = false
        task = Thread {
            while (true) {
                // 投げる
                info("投げる")
                Pigg.send(AreaGamePlayPacket().apply { id = 308; data = "02".fromHexToBytes() })
                Pigg.send(AreaGamePlayPacket().apply { id = 156; data = "00".fromHexToBytes() })

                if (ICE_LAKE.mainWindow?.autoUoUo?.isSelected == true) {
                    Pigg.proxies[ServerType.CHAT]?.send(ByteBuffer.wrap("00 10 00 00 00 00 30 08 00 00 00 05 00 00 01 0d 01".fromHexToBytes()))
                }

                Thread.sleep(5000)

                // 魚が来る
                while (!kita) {
                    info("魚が来る")
                    Pigg.send(AreaGamePlayPacket().apply { id = 308; data = "03".fromHexToBytes() })
                    Pigg.send(AreaGamePlayPacket().apply { id = 260 })

                    Thread.sleep(500)
                }

                kita = false

                Thread.sleep(2000)

                // 釣る
                info("釣る")
                Pigg.send(AreaGamePlayPacket().apply { id = 308; data = "04".fromHexToBytes() })
                Pigg.send(AreaGamePlayPacket().apply { id = 268; data = "00 00 00 00".fromHexToBytes() })

                Thread.sleep(2000)

                val delay = max(0, (lastFish + 15_300) - System.currentTimeMillis())
                info("待機 $delay ms")
                Thread.sleep(delay)
                lastFish = System.currentTimeMillis()

                // 釣り上げ
                info("釣り上げ")
                Pigg.send(AreaGamePlayPacket().apply { id = 308; data = "05".fromHexToBytes() })
                Pigg.send(AreaGamePlayPacket().apply {
                    id = 272
                    data = ByteBuilder().apply {
                        writeString("TEST")
                        writeBoolean(true)
                        repeat(13) {
                            writeInt(Random.nextInt(10))
                        }
                        writeDoubleTimeStamp()
                    }.build().array()
                })

                Thread.sleep(1000)

                // リセット
                info("リセット")
                Pigg.send(AreaGamePlayPacket().apply { id = 256 })
                Pigg.send(AreaGamePlayPacket().apply { id = 312 })

                Thread.sleep(1000)
            }
        }

        task?.start()
    }

    fun stop() {
        task?.interrupt()
        task = null
    }

    @EventListener
    fun onPacketSend(event: SendPacketEvent) {
        if (event.packet is AreaGameJoinPacket) {
            ready = true
        }

        if (event.packet is AreaGameLeavePacket) {
            ready = false
            task?.interrupt()
        }

        if (task != null && event.packet is AreaGamePlayPacket) {
            event.packet.canceled = true
        }
    }

    @EventListener
    fun onPacketReceive(event: RecvPacketEvent) {
        val packet = event.packet

        if (task != null && packet is AreaGamePlayResult) {
            if (packet.id == 261.toShort() && packet.data?.toHexString()?.startsWith("02") == true) {
                kita = true
            }
        }
    }
}