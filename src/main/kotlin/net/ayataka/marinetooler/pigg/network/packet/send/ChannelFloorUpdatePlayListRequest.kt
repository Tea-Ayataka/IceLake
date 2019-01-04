package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.toHexString

class ChannelFloorUpdatePlayListRequest : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.CHANNEL_FLOOR_UPDATE_PLAYLIST_REQUEST.id
    override val encrypted = true

    var userCode = ""
    var index = 0
    var type = 0
    var videoID = ""
    var videoTitle = ""
    var videoSource = ""
    var duration = 0
    var data = ByteArray(0)

    override fun readFrom(buffer: ByteBuilder) {
        type = buffer.readInt()

        if(type != 1){
            return
        }

        userCode = buffer.readString()
        index = buffer.readInt()
        videoID = buffer.readString()
        videoTitle = buffer.readString()
        duration = buffer.readInt()
        videoSource = buffer.readString()
        data = buffer.readAllBytes()

        println("$userCode $index $videoID $videoTitle $duration $videoSource")

        val fuck = buffer.array()

        dump(String(fuck))
        dump(fuck.toHexString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        if(type != 1){
            return null
        }

        buffer.writeInt(type)
        buffer.writeString(userCode)
        buffer.writeInt(index)
        buffer.writeString(videoID)
        buffer.writeString(videoTitle)
        buffer.writeInt(duration)
        buffer.writeString(videoSource)
        buffer.writeRawBytes(data)

        return buffer
    }
}