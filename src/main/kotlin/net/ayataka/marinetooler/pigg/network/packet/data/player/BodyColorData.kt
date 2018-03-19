package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder

class BodyColorData {
    var skin: Short = 2
    var hair: Short = 2
    var eyebrow: Short = 1
    var eye: Short = 0
    var beard: Short = 1
    var lip: Short = -1
    var cheek: Short = -1
    var eyeshadow: Short = -1

    fun readFrom(buffer: ByteBuilder){
        skin = buffer.readShort()
        hair = buffer.readShort()
        eyebrow = buffer.readShort()
        eye = buffer.readShort()
        beard = buffer.readShort()
        lip = buffer.readShort()
        cheek = buffer.readShort()
        eyeshadow = buffer.readShort()
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeShort(skin)
                .writeShort(hair)
                .writeShort(eyebrow)
                .writeShort(eye)
                .writeShort(beard)
                .writeShort(lip)
                .writeShort(cheek)
                .writeShort(eyeshadow)
    }

    fun clone(): BodyColorData{
        return this
    }
}