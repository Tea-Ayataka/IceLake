package net.ayataka.marinetooler.pigg.network.packet

import java.nio.ByteBuffer
import java.nio.ByteOrder

open class ByteBuilder() {
    // This may throws BufferOverflowException
    private var buffer: ByteBuffer = ByteBuffer.allocate(1024 * 128)

    constructor(buf: ByteBuffer) : this() {
        buffer = ByteBuffer.wrap(buf.array()) // Clone buffer
    }

    constructor(bytes: ByteArray) : this() {
        buffer = ByteBuffer.wrap(bytes)
    }

    fun reset(): ByteBuilder {
        buffer.clear()
        return this
    }

    fun array(): ByteArray {
        return buffer.array()
    }

    fun skip(index: Int): ByteBuilder {
        buffer.position(buffer.position() + index)
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
        val buf = ByteBuffer.wrap(readRawBytes(4))
        buf.order(ByteOrder.LITTLE_ENDIAN)
        return buf.int
    }

    fun readRawLong(): Long {
        return buffer.long
    }

    fun readRawBytes(length: Int): ByteArray {
        val result = ByteArray(length)
        buffer.get(result, 0, length)
        return result
    }

    fun readFloat(): Float {
        return buffer.float
    }

    // Writers
    fun writeRawByte(byte: Byte): ByteBuilder {
        buffer.put(byte)
        return this
    }

    fun writeRawBytes(byteArray: ByteArray): ByteBuilder {
        buffer.put(byteArray, 0, byteArray.size)
        return this
    }

    fun writeRawShort(short: Short): ByteBuilder {
        buffer.putShort(short)
        return this
    }

    fun writeRawInt(int: Int): ByteBuilder {
        buffer.putInt(int)
        return this
    }

    fun writeRawLong(long: Long): ByteBuilder {
        buffer.putLong(long)
        return this
    }

    fun writeRawDouble(double: Double): ByteBuilder {
        buffer.putDouble(double)
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
        return String(readRawBytes(length.toInt()))
    }

    fun readString(length: Int): String {
        return String(readRawBytes(length))
    }

    fun readBytes(): ByteArray {
        val length = readShort()
        return readRawBytes(length.toInt())
    }

    fun readAllBytes(): ByteArray {
        val length = buffer.limit() - buffer.position()
        return readRawBytes(length)
    }

    fun readBoolean(): Boolean {
        return buffer.get().toInt() > 0
    }

    // Writers
    fun writeString(text: String): ByteBuilder {
        writeRawShort(text.toByteArray().size.toShort())

        if (text.toByteArray().isEmpty()) {
            writeRawByte(0)
        } else {
            writeRawBytes(text.toByteArray())
        }
        return this
    }

    fun writeRawString(usercode: String): ByteBuilder {
        writeRawBytes(usercode.toByteArray())
        return this
    }

    fun writeBytes(array: ByteArray): ByteBuilder {
        writeRawShort(array.size.toShort())
        writeRawBytes(array)
        return this
    }

    fun writeTimeStamp(): ByteBuilder {
        writeRawLong(System.currentTimeMillis())
        return this
    }

    fun writeDoubleTimeStamp(): ByteBuilder {
        writeRawDouble(System.currentTimeMillis().toDouble())
        return this
    }

    fun writeBoolean(value: Boolean): ByteBuilder {
        buffer.put((if (value) 1 else 0).toByte())
        return this
    }

    fun writeRawFloat(float: Float): ByteBuilder {
        buffer.putFloat(float)
        return this
    }
}