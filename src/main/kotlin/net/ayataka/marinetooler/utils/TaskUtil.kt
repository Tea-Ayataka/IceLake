package net.ayataka.marinetooler.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.EmptyCoroutineContext

fun runLater(f: () -> Unit, milliseconds: Long) {
    val timerTask = object : TimerTask() {
        override fun run() {
            f()
        }
    }

    val timer = Timer()

    timer.schedule(timerTask, milliseconds)
}

fun CoroutineScope.start(action: suspend CoroutineScope.() -> Unit) {
    launch(context = EmptyCoroutineContext, block = action)
}