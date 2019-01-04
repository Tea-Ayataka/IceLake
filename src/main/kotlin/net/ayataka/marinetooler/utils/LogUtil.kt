package net.ayataka.marinetooler.utils

import net.ayataka.marinetooler.ICE_LAKE
import java.util.logging.Logger

private fun getLogger(): Logger {
    return Logger.getLogger("marinetooler")
}

fun dump(msg: String) {
    println("[DUMP] $msg")
}

fun info(msg: String) {
    println("   [INFO] $msg")
    ICE_LAKE.mainWindow?.log(msg)
}

fun warn(msg: String) {
    println("   [ERROR] $msg")
}

fun severe(msg: String) {
    getLogger().severe(msg)
}