package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.math.Vec3i
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.*
import kotlin.concurrent.timer

object Command : Module() {

    @EventListener
    fun onSendPacket(event: SendPacketEvent) {
        val packet = event.packet

        if (packet is TalkPacket) {
            val text = packet.text

            if (text.startsWith(".")) {
                event.packet = CancelTypingPacket()
                doCommand(text.substring(1))
            }
        }
    }

    var fucker: Timer? = null
    var actionSpammer: Timer? = null
    var petTimer: Timer? = null
    var eventNumber = 0

    fun doCommand(string: String) {
        try {
            val spitted = string.split(" ")

            when (spitted[0].toLowerCase()) {
                "act" -> {
                    CurrentUser.playAction(spitted[1])
                }
                "sact" -> {
                    CurrentUser.playSystemAction(spitted[1])
                }
                "area" -> {
                    val packet = GetAreaPacket()
                    packet.category = spitted[1].split("/")[0]
                    packet.code = spitted[1].split("/")[1]
                    Pigg.send(packet)
                }
                "travel" -> {
                    val packet = TravelBundlePacket()
                    packet.categoryCode = spitted[1]
                    packet.areaCode = spitted[2]
                    Pigg.send(packet)
                }
                "shop" -> {
                    val packet = GetShopPacket()
                    packet.shopCode = spitted[1]
                    Pigg.send(packet)
                }
                "addfish" -> {
                    AquariumSpammer.addFish(spitted[1])
                }
                "placea" -> {
                    val packet = PlaceActionItem()
                    packet.code = spitted[1]
                    packet.x = 10
                    packet.y = 10

                    Pigg.send(packet)
                }
                "spam" -> {
                    NoticeSpammer.enabled = !NoticeSpammer.enabled
                }
                "m" -> {
                    FurnitureExploiter.move(-1, spitted[1].toInt(), spitted[2].toInt(), spitted[3].toInt(), spitted[4].toByte())
                }
                "place" -> {
                    FurnitureExploiter.place(spitted[1], spitted[2].toInt(), spitted[3].toInt(), spitted[4].toInt(), spitted[5].toByte())
                }
                "del" -> {
                    FurnitureExploiter.remove(spitted[1].toInt())
                }
                "sendpacket" -> {
                    if (spitted[1] == "info") {
                        Pigg.proxies[ServerType.INFO]?.send(ByteBuffer.wrap(spitted.drop(2).joinToString(" ").fromHexToBytes()))
                    } else if (spitted[1] == "chat") {
                        Pigg.proxies[ServerType.CHAT]?.send(ByteBuffer.wrap(spitted.drop(2).joinToString(" ").fromHexToBytes()))
                    }
                }
                "rod" -> {
                    Thread {
                        repeat(1000) {
                            Pigg.proxies[ServerType.INFO]?.send(ByteBuffer.wrap("00 10 00 00 00 00 18 05 00 00 00 3c 01 00 09 69 6e 63 65 6e 74 69 76 65 00 07 6f 6e 53 74 61 72 74 00 03 72 6f 64 00 1c 69 73 68 69 6b 61 72 69 67 61 77 61 5f 66 69 73 68 69 6e 67 5f 72 6f 64 30 31 5f 70 00 00 00 03".fromHexToBytes()))
                            Thread.sleep(50)
                        }
                    }.start()
                }
                "areadata" -> {
                    info(CurrentUser.areaData.toString())
                }
                "'" -> {
                    Pigg.send(TravelBundlePacket().apply { categoryCode = "event"; areaCode = "event$eventNumber" })
                    info("event event$eventNumber")
                    eventNumber++
                }
                "zigzag" -> {
                    if (spitted[1] == "start") {
                        fucker = timer(period = 50) {
                            val maxX = CurrentUser.areaData.areaData.sizeX.toInt()
                            val maxY = CurrentUser.areaData.areaData.sizeY.toInt()

                            ClickTP.zigzag(Vec3i(SecureRandom().nextInt(maxX), SecureRandom().nextInt(maxY), 0))
                        }
                    }

                    if (spitted[1] == "stop") {
                        ClickTP.moving = false
                        fucker?.cancel()
                    }
                }
                "kurukuru" -> {
                    if (ClickTP.rotater == null) {
                        ClickTP.rotateStart()
                    } else {
                        ClickTP.rotateStop()
                    }
                }
                "onemsg" -> {
                    val packet = OneMessageSavePacket()
                    packet.text = spitted[1].replace("\\n", "\n")
                    Pigg.send(packet)
                }
                "roomact" -> {
                    val packet = RoomActionPacket()
                    packet.actionCode = spitted[1]

                    Pigg.send(packet)
                }
                "listaction" -> {
                    Pigg.send(ListActionPacket())
                }
                "hackpet" -> {
                    HackPet.enabled = !HackPet.enabled
                }
                "petzigzag" -> {
                    if (spitted[1] == "start" && CurrentUser.selectedPetId != null) {
                        petTimer = timer(period = 50) {
                            val maxX = CurrentUser.areaData.areaData.sizeX.toInt()
                            val maxY = CurrentUser.areaData.areaData.sizeY.toInt()

                            HackPet.teleport(CurrentUser.selectedPetId!!, SecureRandom().nextInt(maxX).toShort(), SecureRandom().nextInt(maxY).toShort(), 0, 0)
                        }
                    }
                    if (spitted[1] == "stop") {
                        petTimer?.cancel()
                    }
                }
                "equip" -> {
                    when(spitted[1]){
                        "add" -> FakeEquipment.addEquipment(CurrentUser.usercode!!, spitted[2])
                        "del" -> FakeEquipment.deleteEquipment(CurrentUser.usercode!!, spitted[2])
                    }
                }
                else -> {
                    info("無効なコマンドです")
                }
            }
        } catch (ex: Exception) {
            info("エラー: ${ex.javaClass.name}")
        }
    }
}