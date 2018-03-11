package net.ayataka.marinetooler.pigg.network.packet.data

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

open class ShopGachaData {
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

    fun readFrom(buffer: ByteBuilder) {
        this.code = buffer.readString()
        this.name = buffer.readString()
        this.payType = buffer.readByte()
        this.price = buffer.readInt()
        this.isDisEnableCoupon = buffer.readBoolean()
        this.time = buffer.readDouble()
        this.remainingFreePlayCount = buffer.readInt()

        this.freePlayStartDate = buffer.readDouble()
        this.freePlayEndDate = buffer.readDouble()

        this.isNoOverlap = buffer.readBoolean()
        this.noOverlapRemainTime = buffer.readDouble()
        this.isStepup = buffer.readBoolean()

        this.noOverlapEternalMode = this.isNoOverlap && this.noOverlapRemainTime == 0.0

        this.item = GachaItemData()
        this.item.readFrom(buffer)

        this.isNormalBonus = buffer.readBoolean()
        this.freePlayCountLabelState = buffer.readByte()
        this.noOverlapLabelState = buffer.readByte()
        this.salePrice = buffer.readInt()
        this.normalBonusGachaStatus = buffer.readByte()
        this.stepUpGachaStatus = buffer.readByte()
        this.consecutiveGachaStatus = buffer.readByte()
    }
}