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
        face = buffer.readShort()
        hairFront = buffer.readShort()
        hairBack = buffer.readShort()
        eye = buffer.readShort()
        eyebrow = buffer.readShort()
        nose = buffer.readShort()
        mouth = buffer.readShort()
        beard = buffer.readShort()
        mole1 = buffer.readShort()
        mole2 = buffer.readShort()
        option = buffer.readShort()
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeByte(gender)
                .writeShort(face)
                .writeShort(hairFront)
                .writeShort(hairBack)
                .writeShort(eye)
                .writeShort(eyebrow)
                .writeShort(nose)
                .writeShort(mouth)
                .writeShort(beard)
                .writeShort(mole1)
                .writeShort(mole2)
                .writeShort(option)
    }
}