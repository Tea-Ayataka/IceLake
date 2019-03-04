package net.ayataka.marinetooler.pigg

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.pigg.network.packet.data.area.BaseAreaData
import net.ayataka.marinetooler.pigg.network.packet.recv.AlertResultPacket
import net.ayataka.marinetooler.pigg.network.packet.send.*
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.math.Vec3i

object CurrentUser {
    var usercode: String? = null
    var secure: ByteArray? = null
    var areaData = BaseAreaData()
    var location = Vec3i()
    var selectedPetId: Int? = null

    init {
        EventManager.register(this)
    }

    fun showAlert(msg: String) {
        val packet = AlertResultPacket()
        packet.message = "# Marine Tooler #\n$msg\n\n\n"
        PiggProxy.receive(packet)
    }

    fun move(x: Int, y: Int, z: Int) {
        val packet = MovePacket()
        packet.x = x.toShort()
        packet.y = y.toShort()
        packet.z = z.toShort()
        PiggProxy.send(packet)
    }

    fun teleport(x: Int, y: Int, z: Int, direction: Byte) {
        val packet = MoveEndPacket()
        packet.x = x.toShort()
        packet.y = y.toShort()
        packet.z = z.toShort()
        packet.direction = direction
        PiggProxy.send(packet)
    }

    fun playAction(actionCode: String) {
        val packet = ActionPacket()
        packet.actionId = "$actionCode\u0000_secret"
        PiggProxy.send(packet)
    }

    fun playSystemAction(actionCode: String) {
        PiggProxy.send(SystemActionPacket().apply { this.actionCode = actionCode })
    }

    fun giveGood(ucode: String) {
        val profile = GetUserProfilePacket()
        profile.usercode = ucode
        PiggProxy.send(profile)

        Thread.sleep(300)

        val goodPigg = GoodPiggPacket()
        goodPigg.usercode = ucode
        PiggProxy.send(goodPigg)
    }

    fun spinSlot() {
        val packet = TableGamePacket()
        packet.method = "spin"
        packet.data = "00 00 00 14 00 00 00 05 ".fromHexToBytes()
        PiggProxy.send(packet)
    }
}