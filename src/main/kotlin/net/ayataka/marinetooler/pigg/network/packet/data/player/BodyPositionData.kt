package net.ayataka.marinetooler.pigg.network.packet.data.player

import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.utils.math.Vec2f

class BodyPositionData {
    var eye = Vec2f()
    var eyebrow = Vec2f()
    var cheek = Vec2f()
    var nose = Vec2f()
    var mouth = Vec2f()
    var beard = Vec2f()
    var mole1 = Vec2f()
    var mole2 = Vec2f()

    fun readFrom(buffer: ByteBuilder) {
        eye = Vec2f(buffer.readFloat(), buffer.readFloat())
        eyebrow = Vec2f(buffer.readFloat(), buffer.readFloat())
        cheek = Vec2f(buffer.readFloat(), buffer.readFloat())
        nose = Vec2f(buffer.readFloat(), buffer.readFloat())
        mouth = Vec2f(buffer.readFloat(), buffer.readFloat())
        beard = Vec2f(buffer.readFloat(), buffer.readFloat())
        mole1 = Vec2f(buffer.readFloat(), buffer.readFloat())
        mole2 = Vec2f(buffer.readFloat(), buffer.readFloat())
    }

    fun writeTo(buffer: ByteBuilder): ByteBuilder {
        buffer.writeFloat(eye.x, eye.y)
                .writeFloat(eyebrow.x, eyebrow.y)
                .writeFloat(cheek.x, cheek.y)
                .writeFloat(nose.x, nose.y)
                .writeFloat(mouth.x, mouth.y)
                .writeFloat(beard.x, beard.y)
                .writeFloat(mole1.x, mole1.y)
                .writeFloat(mole2.x, mole2.y)

        return buffer
    }
}
