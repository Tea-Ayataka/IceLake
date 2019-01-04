package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.pigg.network.packet.data.user.AvatarData
import net.ayataka.marinetooler.utils.toHexString

//TODO: Refactor
class FinishDressupResult : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.FINISH_DRESSUP_RESULT.id

    var usercode = ""
    var avatarData: AvatarData? = null
    var isFinishDressUp = false

    override fun readFrom(buffer: ByteBuilder) {
        if(buffer.array().size <= 13){
            isFinishDressUp = true

            return
        }

        if(buffer.readBoolean()){
            avatarData = AvatarData().apply { readFrom(buffer) }
            usercode = avatarData?.userCode!!

            CurrentUser.avatarData = avatarData!!

            return
        }

        usercode = buffer.readString()
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        if(isFinishDressUp){
            isFinishDressUp = !isFinishDressUp

            return null
        }

        buffer.writeBoolean(avatarData != null)

        avatarData?.let {
            it.writeTo(buffer)

            return buffer
        }

        buffer.writeString(usercode)

        return buffer
    }
}