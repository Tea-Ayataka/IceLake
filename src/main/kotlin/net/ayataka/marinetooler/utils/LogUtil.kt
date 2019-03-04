package net.ayataka.marinetooler.utils

import net.ayataka.marinetooler.ICE_LAKE
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("IceLake")

fun trace(text: String) {
    LOGGER.trace(text)
}

fun info(text: String) {
    LOGGER.info(text)

    try {
        ICE_LAKE.mainWindow?.log("[INFO] $text")
    } catch (ex: Exception) {
    }
}

fun error(text: String, ex: Exception? = null) {
    if (ex == null) {
        LOGGER.error(text)
        ICE_LAKE.mainWindow?.log("[ERROR] $text")
    } else {
        LOGGER.error(text, ex)
        ICE_LAKE.mainWindow?.log("[ERROR] $text : ${ex.message} ${ex.javaClass.name}\n    ${ex.stackTrace.joinToString("\n    ")}")
    }
}