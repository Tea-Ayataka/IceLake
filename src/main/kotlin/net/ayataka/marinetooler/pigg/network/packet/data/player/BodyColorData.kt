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
        this.skin = buffer.readShort()
        this.hair = buffer.readShort()
        this.eyebrow = buffer.readShort()
        this.eye = buffer.readShort()
        this.beard = buffer.readShort()
        this.lip = buffer.readShort()
        this.cheek = buffer.readShort()
        this.eyeshadow = buffer.readShort()
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder{
        return buffer
                .writeRawShort(skin)
                .writeRawShort(hair)
                .writeRawShort(eyebrow)
                .writeRawShort(eye)
                .writeRawShort(beard)
                .writeRawShort(lip)
                .writeRawShort(cheek)
                .writeRawShort(eyeshadow)
    }

    fun clone(): BodyColorData{
        return this
    }
}