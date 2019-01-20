package net.ayataka.marinetooler.pigg.network.packet.send

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.trace

class LoginPacket : Packet() {
    override val server = ServerType.INFO
    override val packetId = InfoPacketID.LOGIN.id
    override val encrypted = true

    var ticket = ""
    var amebaId = ""
    var password = ""
    var fromAndroid = false
    var agent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.2; WOW64; Trident/7.0; .NET4.0C; .NET4.0E)"
    var flashplayerVersion = "29.0"
    var amebaAuthTicket = ""
    var frmId = ""
    var p = "9aac7a5da0605533b1d33f5f423bbd1a"
    var ph = ""

    override fun readFrom(buffer: ByteBuilder) {
        ticket = buffer.readString()
        amebaId = buffer.readString()
        password = buffer.readString()
        fromAndroid = buffer.readBoolean()
        agent = buffer.readString()
        flashplayerVersion = buffer.readString()
        amebaAuthTicket = buffer.readString()
        frmId = buffer.readString()
        p = buffer.readString()
        ph = buffer.readString()

        trace("wtf? ${buffer.readAllBytes().size}")

        trace(" Huh? Ticket: $ticket amebaId: $amebaId password: $password fromAndroid: $fromAndroid  Agent: $agent flashPlayerVersion: $flashplayerVersion amebaAuthTicket: $amebaAuthTicket frmId: $frmId p: $p ph: $ph")
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(ticket, amebaId, password)
        buffer.writeBoolean(fromAndroid)
        buffer.writeString(agent, flashplayerVersion, amebaAuthTicket, frmId, p, ph)
        return buffer
    }

    /*fun fuck() {
        ph = l(p, frmId, amebaAuthTicket, flashplayerVersion, agent, fromAndroid.toString(), password, amebaId, ticket)
        ph = j(ticket, amebaId, password, fromAndroid.toString(), agent, flashplayerVersion, amebaAuthTicket, frmId, p, ph)
        p = p.substring(0, 32)

        println("FUCKED p: $p ph: $ph")
    }

    private fun l(vararg params: String): String {
        var result = ""
        for (i in 0 until params.size) {
            val var1 = if(params[i].length > 32) params[i].substring(0, 32) else params[i]
            result += if (i == 0) DigestUtils.md5Hex(var1) else var1
        }

        return result
    }

    private fun j(vararg args: String): String {
        val var1 = args[6].substring(0, 32) + args[0].substring(0, 1) + args[3].substring(0, 1) + args[4].substring(0, 1) + args[5].substring(0, 1) + args[8].substring(0, 32)
        val var2 = d(var1) + args[9].substring(0, 32) + System.currentTimeMillis()
        return d(var2)
    }

    private fun d(vararg args: String): String {
        return Base64.getEncoder().encode(c(args[0]).toByteArray()).toString(Charset.defaultCharset())
    }

    private fun c(vararg args: String): String {
        var var1 = ""
        var var2 = 0
        var var3 = "GehBraumua><mvaals;dkfj02348u:JKLHL:KJHfdaoifullD!C?Z12038%9i*14u12[409hj;3"

        while (var2 < args[0].length) {
            if (var2 > var3.length - 1) {
                var3 += var3
            }

            var1 += Character.toString((args[0][var2].toByte() xor var3[var2].toByte()).toChar())
            var2 += 1
        }
        return var1
    }*/
}