package net.ayataka.marinetooler.module.impl

import net.ayataka.eventapi.EventListener
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.pigg.event.SendPacketEvent
import net.ayataka.marinetooler.pigg.network.packet.recv.GetAreaResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.math.Vec3i
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
                doCommand(text.substring(1))
                event.canceled = true
                PiggProxy.send(CancelTypingPacket())
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
                    PiggProxy.send(packet)
                }
                "travel" -> {
                    val packet = TravelBundlePacket()
                    packet.categoryCode = spitted[1]
                    packet.areaCode = spitted[2]
                    PiggProxy.send(packet)
                }
                "shop" -> {
                    val packet = GetShopPacket()
                    packet.shopCode = spitted[1]
                    PiggProxy.send(packet)
                }
                "addfish" -> {
                    AquariumSpammer.addFish(spitted[1])
                }
                "placea" -> {
                    val packet = PlaceActionItem()
                    packet.code = spitted[1]
                    packet.x = 10
                    packet.y = 10

                    PiggProxy.send(packet)
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
                "areadata" -> {
                    info(CurrentUser.areaData.toString())
                }
                "'" -> {
                    PiggProxy.send(TravelBundlePacket().apply { categoryCode = "event"; areaCode = "event$eventNumber" })
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
                    PiggProxy.send(packet)
                }
                "roomact" -> {
                    val packet = RoomActionPacket()
                    packet.actionCode = spitted[1]

                    PiggProxy.send(packet)
                }
                "listaction" -> {
                    PiggProxy.send(ListActionPacket())
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
                    when (spitted[1]) {
                        "add" -> FakeEquipment.addEquipment(CurrentUser.usercode!!, spitted[2])
                        "del" -> FakeEquipment.deleteEquipment(CurrentUser.usercode!!, spitted[2])
                    }
                }
                "play" -> {
                    PlaylistModifer.enabled = true

                    PlaylistModifer.videoID = spitted[1]
                    spitted.getOrNull(2)?.let { PlaylistModifer.title = it }

                    CurrentUser.showAlert("何でもいいのでプレイリストに追加してください。")
                }
                "makeover" -> {
                    MakeOverEverywhere.enabled = !MakeOverEverywhere.enabled
                }
                "icearea" -> {
                    CurrentUser.showAlert("Moving to IceArea!")

                    //IceAreaConnector.areaData = CurrentUser.areaData

                    val packet = GetAreaResultPacket().apply {
                        type = "secret"
                        userCode = "test_001"
                        protocol = "ws"
                        chatServerUri = "ws://localhost:11451/command"
                    }

                    //Pigg.receive((Pigg.proxies[ServerType.INFO]!!.packetListener!! as InfoPacketListener).onReceive(packet))
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