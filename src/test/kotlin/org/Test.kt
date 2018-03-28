package org

import net.ayataka.marinetooler.pigg.network.Protocol
import net.ayataka.marinetooler.pigg.network.ServerType
import net.ayataka.marinetooler.pigg.network.packet.ByteBuilder
import net.ayataka.marinetooler.pigg.network.packet.send.PlayGachaStepupPacket
import net.ayataka.marinetooler.utils.fromHexToBytes

fun main(args: Array<String>) {
    val raw = "00 10 00 00 00 00 19 09 00 00 00 20 00 16 67 61 63 68 61 5f 73 74 65 70 5f 73 6e 6f 77 6d 6f 6d 6f 6e 67 61 00 01 00 00 00 00 3a 00".fromHexToBytes()

}