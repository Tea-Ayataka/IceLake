package net.ayataka.marinetooler.module.impl

import net.ayataka.marinetooler.ICE_LAKE
import net.ayataka.marinetooler.module.Module
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.pigg.network.packet.send.NotifyUserRoomEnteredPacket
import java.util.*
import kotlin.concurrent.timer

object NoticeSpammer : Module() {
    var timer: Timer? = null

    override fun onEnable() {
        timer?.cancel()

        timer = timer(period = 100) {
            Pigg.send(NotifyUserRoomEnteredPacket().apply { userCode = ICE_LAKE.targetUser })
        }
    }

    override fun onDisable() {
        timer?.cancel()
    }
}