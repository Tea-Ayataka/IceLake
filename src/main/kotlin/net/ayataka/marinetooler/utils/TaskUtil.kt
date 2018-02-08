package net.ayataka.marinetooler.utils

import java.util.*

fun runLater(f: () -> Unit, milliseconds: Long) {
    val timerTask = object : TimerTask() {
        override fun run() {
            f()
        }
    }

    val timer = Timer()

    timer.schedule(timerTask, milliseconds)
}