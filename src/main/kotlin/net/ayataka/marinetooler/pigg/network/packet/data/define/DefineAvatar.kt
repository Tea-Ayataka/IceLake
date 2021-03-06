package net.ayataka.marinetooler.pigg.network.packet.data.define

import net.ayataka.marinetooler.pigg.network.packet.data.area.PartData
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData

class DefineAvatar(data: AvatarData? = null) : DefineData() {
    var part = PartData(false)
    var data = AvatarData()
    var friend = false

    init {
        if (data != null) {
            characterId = data.userCode

            name = if (data.amebaId.isEmpty()) {
                data.nickName
            } else {
                data.amebaId
            }

            this.data = data
        }
    }

    override fun toString(): String {
        return "DefineAvatar(part=$part, data=$data, friend=$friend)"
    }
}