package net.ayataka.marinetooler.pigg.network.packet

import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.toHexString
import java.nio.ByteBuffer
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

abstract class Packet {
    var canceled = false
    open val encrypted = false

    abstract val server: ServerType
    abstract val packetId: Short

    protected abstract fun readFrom(buffer: ByteBuilder)
    protected abstract fun writeTo(buffer: ByteBuilder): ByteBuilder?

    fun read(buffer: ByteBuilder) {
        if (encrypted) {
            buffer.pos(10) // Move pos to get body length
            val body = decrypt(buffer.readBytes())
            val decrypted = ByteBuilder().writeRawBytes(body)
            readFrom(ByteBuilder(decrypted.build()))
        } else {
            buffer.skipHeader()
            readFrom(buffer)
        }
    }

    fun write(): ByteBuffer? {
        return format(writeTo(ByteBuilder()))
    }

    private fun format(buffer: ByteBuilder?): ByteBuffer? {
        if (buffer == null) {
            return null
        }

        var body = buffer.build().array()
        val formatted = ByteBuilder()

        // Write header
        writeHeader(formatted)

        // Write encrypted body with length
        if (encrypted) {
            body = encrypt(body)
        }
        formatted.writeShort(body.size.toShort())
        formatted.writeRawBytes(body)

        val debug = formatted.build()
        dump("BUILT PACKET")
        dump(debug.array().toHexString())
        dump(String(debug.array()))
        return debug
    }

    private fun writeHeader(buffer: ByteBuilder) {
        buffer.writeRawBytes("00 10 00 00 00 00".fromHexToBytes())
        buffer.writeShort(packetId)
        buffer.writeRawBytes("00 00".fromHexToBytes())
    }

    private fun decrypt(source: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(Protocol.cipherKey[server], "DES"))
        return cipher.doFinal(source)
    }

    private fun encrypt(source: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(Protocol.cipherKey[server], "DES"))
        return cipher.doFinal(source)
    }
}