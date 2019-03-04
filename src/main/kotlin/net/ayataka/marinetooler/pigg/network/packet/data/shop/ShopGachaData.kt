package net.ayataka.marinetooler.pigg.network.packet.data.shop

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class ShopGachaData : PacketData {
    var code: String = ""
    var name: String = ""
    var payType: Byte = 0
    var price: Int = 0
    var item = GachaItemData()
    var isDisEnableCoupon: Boolean = false
    var remainingFreePlayCount: Int = 0
    var freePlayStartDate: Double = 0.0
    var freePlayEndDate: Double = 0.0
    var time: Double = 0.0
    var isNoOverlap: Boolean = false
    var noOverlapRemainTime: Double = 0.0
    var noOverlapEternalMode: Boolean = false
    var isStepup: Boolean = false
    var isNormalBonus: Boolean = false
    var freePlayCountLabelState: Byte = 0
    var noOverlapLabelState: Byte = 0
    var salePrice: Int = 0
    var normalBonusGachaStatus: Byte = 0
    var stepUpGachaStatus: Byte = 0
    var consecutiveGachaStatus: Byte = 0

    override fun readFrom(buffer: ByteBuilder) {
        code = buffer.readString()
        name = buffer.readString()
        payType = buffer.readByte()
        price = buffer.readInt()
        isDisEnableCoupon = buffer.readBoolean()
        time = buffer.readDouble()
        remainingFreePlayCount = buffer.readInt()

        freePlayStartDate = buffer.readDouble()
        freePlayEndDate = buffer.readDouble()

        isNoOverlap = buffer.readBoolean()
        noOverlapRemainTime = buffer.readDouble()
        isStepup = buffer.readBoolean()

        noOverlapEternalMode = isNoOverlap && noOverlapRemainTime == 0.0

        item = GachaItemData()
        item.readFrom(buffer)

        isNormalBonus = buffer.readBoolean()
        freePlayCountLabelState = buffer.readByte()
        noOverlapLabelState = buffer.readByte()
        salePrice = buffer.readInt()
        normalBonusGachaStatus = buffer.readByte()
        stepUpGachaStatus = buffer.readByte()
        consecutiveGachaStatus = buffer.readByte()
    }

    override fun writeTo(buffer: ByteBuilder) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}