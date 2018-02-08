package net.ayataka.marinetooler.utils

import net.ayataka.marinetooler.Tooler
import java.util.logging.Logger

private fun getLogger(): Logger {
    return Logger.getLogger("marinetooler")
}

fun dump(msg: String) {
    println("[DUMP] $msg")
}

fun info(msg: String) {
    println("   [INFO] $msg")
    Tooler.mainWindow?.log(msg)
}

fun warn(msg: String) {
    println("   [ERROR] $msg")
}

fun severe(msg: String) {
    getLogger().severe(msg)
}