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
        this.eye = Point(buffer.readFloat(), buffer.readFloat())
        this.eyebrow = Point(buffer.readFloat(), buffer.readFloat())
        this.cheek = Point(buffer.readFloat(), buffer.readFloat())
        this.nose = Point(buffer.readFloat(), buffer.readFloat())
        this.mouth = Point(buffer.readFloat(), buffer.readFloat())
        this.beard = Point(buffer.readFloat(), buffer.readFloat())
        this.mole1 = Point(buffer.readFloat(), buffer.readFloat())
        this.mole2 = Point(buffer.readFloat(), buffer.readFloat())
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        this.eye.toArray().forEach { buffer.writeRawFloat(it) }
        this.eyebrow.toArray().forEach { buffer.writeRawFloat(it) }
        this.cheek.toArray().forEach { buffer.writeRawFloat(it) }
        this.nose.toArray().forEach { buffer.writeRawFloat(it) }
        this.mouth.toArray().forEach { buffer.writeRawFloat(it) }
        this.beard.toArray().forEach { buffer.writeRawFloat(it) }
        this.mole1.toArray().forEach { buffer.writeRawFloat(it) }
        this.mole2.toArray().forEach { buffer.writeRawFloat(it) }

        return buffer
    }
}
