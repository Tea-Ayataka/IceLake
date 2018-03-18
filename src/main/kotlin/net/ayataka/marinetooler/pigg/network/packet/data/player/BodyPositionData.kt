package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.utils.Point

class BodyPositionData {
    var eye = Point()
    var eyebrow = Point()
    var cheek = Point()
    var nose = Point()
    var mouth = Point()
    var beard = Point()
    var mole1 = Point()
    var mole2 = Point()

    fun readFrom(buffer: ByteBuilder){
        eye = Point(buffer.readFloat(), buffer.readFloat())
        eyebrow = Point(buffer.readFloat(), buffer.readFloat())
        cheek = Point(buffer.readFloat(), buffer.readFloat())
        nose = Point(buffer.readFloat(), buffer.readFloat())
        mouth = Point(buffer.readFloat(), buffer.readFloat())
        beard = Point(buffer.readFloat(), buffer.readFloat())
        mole1 = Point(buffer.readFloat(), buffer.readFloat())
        mole2 = Point(buffer.readFloat(), buffer.readFloat())
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        eye.toArray().forEach { buffer.writeRawFloat(it) }
        eyebrow.toArray().forEach { buffer.writeRawFloat(it) }
        cheek.toArray().forEach { buffer.writeRawFloat(it) }
        nose.toArray().forEach { buffer.writeRawFloat(it) }
        mouth.toArray().forEach { buffer.writeRawFloat(it) }
        beard.toArray().forEach { buffer.writeRawFloat(it) }
        mole1.toArray().forEach { buffer.writeRawFloat(it) }
        mole2.toArray().forEach { buffer.writeRawFloat(it) }

        return buffer
    }
}
