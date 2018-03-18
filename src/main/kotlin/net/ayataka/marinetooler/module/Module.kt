package net.ayataka.marinetooler.module

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.utils.info

open class Module {
    var enabled = false
        set(value) {
            if (value) {
                EventManager.register(this)
                this.onEnable()
                info("Enabled ${this.javaClass.simpleName}")
            } else {
                EventManager.unregister(this)
                this.onDisable()
                info("Disabled ${this.javaClass.simpleName}")
            }

            field = value
        }

    open fun onEnable() {

    }

    open fun onDisable() {

    }
}