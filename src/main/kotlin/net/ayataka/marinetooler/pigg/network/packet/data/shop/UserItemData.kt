package net.ayataka.marinetooler.pigg.network.packet.data.shop

data class UserItemData(
        var item: String,
        var type: String,
        var id: Int,
        var flag: Int
)