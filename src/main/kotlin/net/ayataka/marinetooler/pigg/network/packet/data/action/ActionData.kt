package net.ayataka.marinetooler.pigg.network.packet.data.action

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class ActionData : PacketData {
    var code = ""
    var order = 0
    var title = ""
    var type = ""
    var isDefault = false
    var isUnlocked = false
    var rareRate = 0
    var hint = ""

    override fun readFrom(buffer: ByteBuilder) {
        code = buffer.readString()
        title = buffer.readString()
        type = buffer.readString()

        if ("over_reaction" in type) {
            isUnlocked = buffer.readBoolean()
            rareRate = buffer.readInt()
            hint = buffer.readString()
        }

        order = buffer.readInt()
        isDefault = buffer.readBoolean()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeString(code)
        buffer.writeString(title)
        buffer.writeString(type)

        if ("over_reaction" in type) {
            buffer.writeBoolean(isUnlocked)
            buffer.writeInt(rareRate)
            buffer.writeString(hint)
        }

        buffer.writeInt(order)
        buffer.writeBoolean(isDefault)
    }

    override fun toString(): String {
        return "ActionData(code='$code', order=$order, title='$title', type='$type', isDefault=$isDefault, isUnlocked=$isUnlocked, rareRate=$rareRate, hint='$hint')"
    }
}