package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class BodyPartData {
    var gender: Byte = 0
    var face: Short = 0
    var hairFront: Short = 0
    var hairBack: Short = 0
    var eye: Short = 0
    var eyebrow: Short = 0
    var nose: Short = 0
    var mouth: Short = 0
    var beard: Short = 1
    var mole1: Short = -1
    var mole2: Short = -1
    var option: Short = -1

    fun readFrom(buffer: ByteBuilder){
        this.face = buffer.readShort()
        this.hairFront = buffer.readShort()
        this.hairBack = buffer.readShort()
        this.eye = buffer.readShort()
        this.eyebrow = buffer.readShort()
        this.nose = buffer.readShort()
        this.mouth = buffer.readShort()
        this.beard = buffer.readShort()
        this.mole1 = buffer.readShort()
        this.mole2 = buffer.readShort()
        this.option = buffer.readShort()
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeRawByte(this.gender)
                .writeRawShort(this.face)
                .writeRawShort(this.hairFront)
                .writeRawShort(this.hairBack)
                .writeRawShort(this.eye)
                .writeRawShort(this.eyebrow)
                .writeRawShort(this.nose)
                .writeRawShort(this.mouth)
                .writeRawShort(this.beard)
                .writeRawShort(this.mole1)
                .writeRawShort(this.mole2)
                .writeRawShort(this.option)
    }
}