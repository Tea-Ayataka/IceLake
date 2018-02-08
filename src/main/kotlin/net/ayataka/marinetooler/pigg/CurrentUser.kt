package net.ayataka.marinetooler.pigg

import com.darkmagician6.eventapi.EventManager
import net.ayataka.marinetooler.pigg.network.packet.data.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.AlertResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.fromHexToBytes

object CurrentUser {
    var usercode: String? = null
    var areaData = BaseAreaData()

    init {
        EventManager.register(this)
    }

    fun showAlert(msg: String) {
        val packet = AlertResultPacket()
        packet.message = "# Marine Tooler #\n\n" + msg + "\n"
        Pigg.receive(packet)
    }

    fun move(x: Short, y: Short, z: Short) {
        val packet = MovePacket()
        packet.x = x
        packet.y = y
        packet.z = z
        Pigg.send(packet)
    }

    fun teleport(x: Short, y: Short, z: Short, direction: Byte) {
        val packet = MoveEndPacket()
        packet.x = x
        packet.y = y
        packet.z = z
        packet.dir = direction
        Pigg.send(packet)
    }

    fun playAction(actionCode: String) {
        val packet = ActionPacket()
        packet.actionId = actionCode + "\u0000_secret"
        Pigg.send(packet)
    }

    fun giveGood(ucode: String) {
        val profile = GetUserProfilePacket()
        profile.usercode = ucode
        Pigg.send(profile)

        Thread.sleep(300)

        val goodPigg = GoodPiggPacket()
        goodPigg.usercode = ucode
        Pigg.send(goodPigg)
    }

    fun spinSlot() {
        val packet = TableGamePacket()
        packet.method = "spin"
        packet.data = "00 00 00 14 00 00 00 05 ".fromHexToBytes()
        Pigg.send(packet)
    }
}