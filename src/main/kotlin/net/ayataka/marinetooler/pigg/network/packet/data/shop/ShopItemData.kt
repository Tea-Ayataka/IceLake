package net.ayataka.marinetooler.pigg.network.packet.data.shop

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

open class ShopItemData : PacketData {
    var itemId: String = ""
    var type: String = ""
    var category: String = ""
    var shelf: String = ""
    var name: String = ""
    var description: String = ""
    var stock: Int = 0
    var price: Int = 0
    var originalPrice: Int = 0
    var remainTime: Double = 0.0
    var orderNum: Int = 0
    var quantity: Int = 0
    var sale: Boolean = false
    var bargain: Boolean = false
    var newItem: Boolean = false
    var soldOut: Boolean = false
    var countLimited: Boolean = false
    var termLimited: Boolean = false
    var again: Boolean = false
    var recommended: Boolean = false
    var disableCoupon: Boolean = false
    var maleOnly: Boolean = false
    var femaleOnly: Boolean = false
    var isGiftItem: Boolean = false
    var isGiftOnly: Boolean = false
    var isRequirementMet: Boolean = false
    var isForOppositeGender: Boolean = false
    var isAmeShop: Boolean = false
    var itemPrev: ShopItemData? = null
    var itemNext: ShopItemData? = null
    var setItemData: List<ShopSetItemData> = listOf()
    var seriesCode: String = ""
    var seriesTypeCode: String = ""
    var rarity: Int = 0
    var attack: Int = 0

    override fun readFrom(buffer: ByteBuilder) {
        readData(buffer)
    }

    fun readData(buffer: ByteBuilder, hasType: Boolean = false) {
        shelf = buffer.readString()
        type = buffer.readString()
        category = buffer.readString()
        itemId = buffer.readString()
        name = buffer.readString()
        description = buffer.readString()
        price = buffer.readInt()
        originalPrice = buffer.readInt()
        remainTime = buffer.readDouble()
        stock = buffer.readInt()
        orderNum = buffer.readInt()
        quantity = buffer.readInt()
        newItem = buffer.readBoolean()
        disableCoupon = buffer.readBoolean()
        countLimited = buffer.readBoolean()
        termLimited = buffer.readBoolean()
        again = buffer.readBoolean()
        recommended = buffer.readBoolean()
        sale = buffer.readBoolean()
        bargain = buffer.readBoolean()
        maleOnly = buffer.readBoolean()
        femaleOnly = buffer.readBoolean()
        isGiftItem = buffer.readBoolean()
        isGiftOnly = buffer.readBoolean()
        isRequirementMet = buffer.readBoolean()
        isForOppositeGender = buffer.readBoolean()
        soldOut = buffer.readBoolean()

        setItemData = (0 until buffer.readInt()).map {
            ShopSetItemData().apply { readData(buffer, hasType) }
        }

        if (type == "skill" || type == "battle_item") {
            rarity = buffer.readByte().toInt()
            attack = buffer.readInt()
        }
    }

    override fun writeTo(buffer: ByteBuilder) {
        TODO("not implemented")
    }

    override fun toString(): String {
        return "ShopItemData(itemId='$itemId', type='$type', category='$category', shelf='$shelf', name='$name', description='$description', stock=$stock, price=$price, originalPrice=$originalPrice, remainTime=$remainTime, orderNum=$orderNum, quantity=$quantity, sale=$sale, bargain=$bargain, newItem=$newItem, soldOut=$soldOut, countLimited=$countLimited, termLimited=$termLimited, again=$again, recommended=$recommended, disableCoupon=$disableCoupon, maleOnly=$maleOnly, femaleOnly=$femaleOnly, isGiftItem=$isGiftItem, isGiftOnly=$isGiftOnly, isRequirementMet=$isRequirementMet, isForOppositeGender=$isForOppositeGender, isAmeShop=$isAmeShop, itemPrev=$itemPrev, itemNext=$itemNext, setItemData=$setItemData, seriesCode='$seriesCode', seriesTypeCode='$seriesTypeCode', rarity=$rarity, attack=$attack)"
    }
}