package net.ayataka.marinetooler.utils

import java.net.ServerSocket

fun getUnusedPort(): Int {
    val socket = ServerSocket(0)
    val port = socket.localPort
    socket.close()
    return port
}