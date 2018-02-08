package net.ayataka.marinetooler.pigg.network.packet

import net.ayataka.marinetooler.pigg.CurrentUser
import net.ayataka.marinetooler.utils.dump
import net.ayataka.marinetooler.utils.info

fun readUCodesFromAreaPacket(buffer: ByteBuilder): MutableList<String> {
    val result = mutableListOf<String>()
    var string = String(buffer.array())
    string = string.replace(Regex("[^a-z0-9]+"), " ")

    Regex(" [a-f0-9]{16} ").findAll(string).forEach {
        val code = it.value.replace(" ", "")
        if (!result.contains(code) && code != CurrentUser.usercode) {
            result.add(code)
            dump("UCODE: $code")
        }
    }

    return result
}