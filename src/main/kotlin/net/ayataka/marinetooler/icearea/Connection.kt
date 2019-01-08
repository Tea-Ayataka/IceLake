package net.ayataka.marinetooler.icearea

import net.ayataka.marinetooler.pigg.network.packet.data.define.DefineAvatar
import org.java_websocket.WebSocket

data class Connection(
        val socket: WebSocket,
        val userCode: String,
        var areaCode: String?,
        var avatarDefinition: DefineAvatar
)