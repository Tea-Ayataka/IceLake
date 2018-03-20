package net.ayataka.marinetooler.pigg.network.packet.data.user

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.data.PacketData

class BodyColorData : PacketData {
    var skin: Short = 2
    var hair: Short = 2
    var eyebrow: Short = 1
    var eye: Short = 0
    var beard: Short = 1
    var lip: Short = -1
    var cheek: Short = -1
    var eyeshadow: Short = -1

    override fun readFrom(buffer: ByteBuilder) {
        skin = buffer.readShort()
        hair = buffer.readShort()
        eyebrow = buffer.readShort()
        eye = buffer.readShort()
        beard = buffer.readShort()
        lip = buffer.readShort()
        cheek = buffer.readShort()
        eyeshadow = buffer.readShort()
    }

    override fun writeTo(buffer: ByteBuilder) {
        buffer.writeShort(skin, hair, eyebrow, eye, beard, lip, cheek, eyeshadow)
    }
}