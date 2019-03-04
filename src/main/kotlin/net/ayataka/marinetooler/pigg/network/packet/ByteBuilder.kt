package net.ayataka.marinetooler.pigg.network.packet

import java.nio.ByteBuffer
import java.nio.ByteOrder

open class ByteBuilder() {
    // This may throws BufferOverflowException
    private var buffer: ByteBuffer = ByteBuffer.allocate(1024 * 1024)

    constructor(buf: ByteBuffer) : this() {
        buffer = ByteBuffer.wrap(buf.array()) // Clone buffer
    }

    constructor(bytes: ByteArray) : this() {
        buffer = ByteBuffer.wrap(bytes)
    }

    constructor(size: Int) : this() {
        buffer = ByteBuffer.allocate(size)
    }

    fun reset(): ByteBuilder {
        buffer.clear()
        return this
    }

    fun array(): ByteArray {
        return buffer.array()
    }

    fun skip(length: Int): ByteBuilder {
        buffer.position(buffer.position() + length)
        return this
    }

    fun pos(index: Int): ByteBuilder {
        buffer.position(index)
        return this
    }

    fun getPos(): Int {
        return buffer.position()
    }

    fun build(): ByteBuffer {
        val length = buffer.position()
        buffer.flip()
        val result = ByteBuffer.allocate(length)
        result.put(buffer.array(), 0, length)
        result.flip()
        return result
    }

    // Raw
    // Readers
    fun readByte(): Byte {
        return buffer.get()
    }

    fun readDouble(): Double {
        return buffer.double
    }

    fun readShort(): Short {
        return buffer.short
    }

    fun readInt(): Int {
        return buffer.int
    }

    fun readIntLE(): Int {
        val buf = ByteBuffer.wrap(readBytes(4))
        buf.order(ByteOrder.LITTLE_ENDIAN)
        return buf.int
    }

    fun readRawLong(): Long {
        return buffer.long
    }

    fun readBytes(length: Int): ByteArray {
        val result = ByteArray(length)
        buffer.get(result, 0, length)
        return result
    }

    fun readFloat(): Float {
        return buffer.float
    }

    // Writers
    fun writeByte(vararg bytes: Byte): ByteBuilder {
        bytes.forEach {
            buffer.put(it)
        }
        return this
    }

    fun writeRawBytes(byteArray: ByteArray): ByteBuilder {
        buffer.put(byteArray, 0, byteArray.size)
        return this
    }

    fun writeShort(vararg shorts: Short): ByteBuilder {
        shorts.forEach {
            buffer.putShort(it)
        }
        return this
    }

    fun writeInt(vararg values: Int): ByteBuilder {
        values.forEach { buffer.putInt(it) }
        return this
    }

    fun writeLong(long: Long): ByteBuilder {
        buffer.putLong(long)
        return this
    }

    fun writeDouble(vararg values: Double): ByteBuilder {
        values.forEach { buffer.putDouble(it) }
        return this
    }

    // PIGG Protocol
    fun skipHeader(): ByteBuilder {
        buffer.position(12)
        return this
    }

    // Readers
    fun readString(): String {
        val length = readShort()
        return String(readBytes(length.toInt()))
    }

    fun readString(length: Int): String {
        return String(readBytes(length))
    }

    fun readBytes(): ByteArray {
        val length = readShort()
        return readBytes(length.toInt())
    }

    fun readAllBytes(): ByteArray {
        return readBytes(buffer.remaining())
    }

    fun readBoolean(): Boolean {
        return buffer.get().toInt() > 0
    }

    // Writers
    fun writeString(vararg texts: String): ByteBuilder {
        texts.forEach {
            writeShort(it.toByteArray().size.toShort())
            writeRawBytes(it.toByteArray())
        }

        return this
    }

    fun writeRawString(value: String): ByteBuilder {
        writeRawBytes(value.toByteArray())
        return this
    }

    fun writeBytes(array: ByteArray): ByteBuilder {
        writeShort(array.size.toShort())
        writeRawBytes(array)
        return this
    }

    fun writeTimeStamp(): ByteBuilder {
        writeLong(System.currentTimeMillis())
        return this
    }

    fun writeDoubleTimeStamp(): ByteBuilder {
        writeDouble(System.currentTimeMillis().toDouble())
        return this
    }

    fun writeBoolean(vararg values: Boolean): ByteBuilder {
        values.forEach { buffer.put((if (it) 1 else 0).toByte()) }
        return this
    }

    fun writeFloat(vararg values: Float): ByteBuilder {
        values.forEach { buffer.putFloat(it) }
        return this
    }
}