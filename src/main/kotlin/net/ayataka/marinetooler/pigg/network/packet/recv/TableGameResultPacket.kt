package net.ayataka.marinetooler.pigg.network.packet.recv

import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.id.ChatPacketID
import net.ayataka.marinetooler.pigg.network.id.InfoPacketID
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.Packet
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.toHexString
import java.io.ByteArrayOutputStream
import java.util.zip.Deflater
import java.util.zip.Inflater
import java.util.zip.DeflaterOutputStream

class TableGameResultPacket : Packet() {
    override val server = ServerType.CHAT
    override val packetId = ChatPacketID.TABLE_GAME_RESULT.id

    var method = ""
    var serial = false
    var data: ByteArray? = null

    override fun readFrom(buffer: ByteBuilder) {
        this.method = buffer.readString()
        this.serial = buffer.readBoolean()

        val hasData = buffer.readBoolean()

        if(hasData){
            this.data = buffer.readAllBytes()
        }

        info(" METHOD IS ${this.method} $hasData")

        info(this.serial.toString())
    }

    override fun writeTo(buffer: ByteBuilder): ByteBuilder? {
        buffer.writeString(this.method)
        buffer.writeBoolean(this.serial)

        buffer.writeBoolean(this.data != null)

        this.data?.let { buffer.writeRawBytes(it) }

        return buffer
    }

    private fun compress(byteArray: ByteArray): ByteArray{
        val compresser = Deflater()
        compresser.setLevel(Deflater.BEST_COMPRESSION)
        val compos = ByteArrayOutputStream()
        val dos = DeflaterOutputStream(compos, compresser)

        dos.write(byteArray)

        dos.finish()

        return compos.toByteArray()
    }

    private fun uncompress(byteArray: ByteArray): ByteArray{
        val decompresser = Inflater()

        decompresser.setInput(byteArray)
        val decompos = ByteArrayOutputStream()

        while (!decompresser.finished()) {
            val buf = ByteArray(1024)
            val count = decompresser.inflate(buf)
            decompos.write(buf, 0, count)
        }

        return decompos.toByteArray()
    }
}