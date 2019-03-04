package net.ayataka.marinetooler.pigg.network.packet.data.user

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class BodyPartData : PacketData {
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

    override fun readFrom(buffer: ByteBuilder) {
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

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeByte(gender)
                .writeShort(face, hairFront, hairBack, eye, eyebrow, nose, mouth, beard, mole1, mole2, option)
    }
}