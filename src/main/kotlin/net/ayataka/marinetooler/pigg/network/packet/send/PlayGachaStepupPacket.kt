package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

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
        this.gachaCode = buffer.readString()
        this.useCoupon = buffer.readBoolean()
        this.useFreeAccount = buffer.readBoolean()
        this.spendType = buffer.readByte()
        this.categoryType = buffer.readInt()
        this.playType = buffer.readByte()

        dump("GachaOpen Code: ${this.gachaCode} useCoupon: ${this.useCoupon} useFreeAcc: ${this.useFreeAccount} playType: ${this.playType} spendTime: ${this.spendType} categoryType: ${this.categoryType}")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.gachaCode)
        buffer.writeBoolean(this.useCoupon)
        buffer.writeBoolean(this.useFreeAccount)
        buffer.writeRawByte(this.spendType)
        buffer.writeRawInt(this.categoryType)
        buffer.writeRawByte(this.playType)
        return buffer
    }
}