package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump

class PlayGachaStepupPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.PLAY_GACHA_STEPUP.id

    var gachaCode = ""
    var useCoupon = false
    var useFreeAccount = false
    var playType: Byte = 0
    var spendType: Byte = 0
    var categoryType = 0

    override fun readFrom(buffer: ByteBuilder) {
        gachaCode = buffer.readString()
        useCoupon = buffer.readBoolean()
        useFreeAccount = buffer.readBoolean()
        spendType = buffer.readByte()
        categoryType = buffer.readInt()
        playType = buffer.readByte()

        dump("GachaOpen Code: ${gachaCode} useCoupon: ${useCoupon} useFreeAcc: ${useFreeAccount} playType: ${playType} spendTime: ${spendType} categoryType: ${categoryType}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(gachaCode)
        buffer.writeBoolean(useCoupon)
        buffer.writeBoolean(useFreeAccount)
        buffer.writeByte(spendType)
        buffer.writeInt(categoryType)
        buffer.writeByte(playType)
        return buffer
    }
}