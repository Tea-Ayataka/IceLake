package net.ayataka.marinetooler.pigg.network.packet

import java.nio.ByteBuffer
import java.nio.ByteOrder

open class ByteBuilder() {
    // This may throws BufferOverflowException
    private var buffer: ByteBuffer = ByteBuffer.allocate(1024 * 128)

    constructor(buf: ByteBuffer) : this() {
        this.buffer = ByteBuffer.wrap(buf.array()) // Clone buffer
    }

    constructor(bytes: ByteArray) : this() {
        this.buffer = ByteBuffer.wrap(bytes)
    }

    fun reset(): ByteBuilder {
        this.buffer.clear()
        return this
    }

    fun array(): ByteArray {
        return this.buffer.array()
    }

    fun skip(index: Int): ByteBuilder {
        this.buffer.position(this.buffer.position() + index)
        return this
    }

    fun pos(index: Int): ByteBuilder {
        this.buffer.position(index)
        return this
    }

    fun getPos(): Int {
        return this.buffer.position()
    }

    fun build(): ByteBuffer {
        val length = this.buffer.position()
        this.buffer.flip()
        val result = ByteBuffer.allocate(length)
        result.put(this.buffer.array(), 0, length)
        result.flip()
        return result
    }

    // Raw
    // Readers
    fun readByte(): Byte {
        return this.buffer.get()
    }

    fun readShort(): Short {
        return this.buffer.short
    }

    fun readInt(): Int {
        return this.buffer.int
    }

    fun readIntLE(): Int {
        val buf = ByteBuffer.wrap(this.readRawBytes(4))
        buf.order(ByteOrder.LITTLE_ENDIAN)
        return buf.int
    }

    fun readRawLong(): Long {
        return this.buffer.long
    }

    fun readRawBytes(length: Int): ByteArray {
        val result = ByteArray(length)
        this.buffer.get(result, 0, length)
        return result
    }

    // Writers
    fun writeRawByte(byte: Byte): ByteBuilder {
        this.buffer.put(byte)
        return this
    }

    fun writeRawBytes(byteArray: ByteArray): ByteBuilder {
        this.buffer.put(byteArray, 0, byteArray.size)
        return this
    }

    fun writeRawShort(short: Short): ByteBuilder {
        this.buffer.putShort(short)
        return this
    }

    fun writeRawInt(int: Int): ByteBuilder {
        this.buffer.putInt(int)
        return this
    }

    fun writeRawLong(long: Long): ByteBuilder {
        this.buffer.putLong(long)
        return this
    }

    fun writeRawDouble(double: Double): ByteBuilder {
        this.buffer.putDouble(double)
        return this
    }

    // PIGG Protocol
    fun skipHeader(): ByteBuilder {
        this.buffer.position(12)
        return this
    }

    // Readers
    fun readString(): String {
        val length = this.readShort()
        return String(this.readRawBytes(length.toInt()))
    }

    fun readString(length: Int): String {
        return String(this.readRawBytes(length))
    }

    fun readBytes(): ByteArray {
        val length = this.readShort()
        return this.readRawBytes(length.toInt())
    }

    fun readAllBytes(): ByteArray {
        val length = this.buffer.limit() - this.buffer.position()
        return this.readRawBytes(length)
    }

    fun readBoolean(): Boolean {
        return this.buffer.get().toInt() > 0
    }

    // Writers
    fun writeString(text: String): ByteBuilder {
        this.writeRawShort(text.toByteArray().size.toShort())

        if(text.toByteArray().isEmpty()){
            this.writeRawByte(0)
        }
        else {
            this.writeRawBytes(text.toByteArray())
        }
        return this
    }

    fun writeRawString(usercode: String): ByteBuilder {
        this.writeRawBytes(usercode.toByteArray())
        return this
    }

    fun writeBytes(array: ByteArray): ByteBuilder {
        this.writeRawShort(array.size.toShort())
        this.writeRawBytes(array)
        return this
    }

    fun writeTimeStamp(): ByteBuilder {
        this.writeRawLong(System.currentTimeMillis())
        return this
    }

    fun writeDoubleTimeStamp(): ByteBuilder {
        this.writeRawDouble(System.currentTimeMillis().toDouble())
        return this
    }

    fun writeBoolean(value: Boolean): ByteBuilder {
        this.buffer.put((if (value) 1 else 0).toByte())
        return this
    }
}