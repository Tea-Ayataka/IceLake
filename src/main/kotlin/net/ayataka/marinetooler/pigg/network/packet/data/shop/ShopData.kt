package net.ayataka.marinetooler.pigg.network.packet.data.shop

class ShopData {
    var userCode: String = ""
    var userGender: Int = 0
    var shopCode: String = ""
    var name: String = ""
    var staffDescription: String = ""
    var staffDescription2: String = ""
    var shopTemplateCode: String = ""
    var shopTemplateDomain: String = ""
    var items: List<ShopItemData> = listOf()
    var itemsCount: Int = 0
    var shopType: Int = 0
    var areaName: String = ""
    var gender: Int = 0
    var zone: Int = 0
    var isTutorial: Boolean = false
    var isAreaOpenShop: Boolean = false
    var actionCode: String = ""
    //var coinBannerLinkInfo: ShopCoinBannerLinkInfo
    //var openPanelInfo: String = ""


}