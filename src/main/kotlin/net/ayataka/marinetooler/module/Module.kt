package net.ayataka.marinetooler.module

import net.ayataka.eventapi.EventManager
import net.ayataka.marinetooler.utils.info

open class Module {
    var enabled = false
        set(value) {
            if (value && !field) {
                EventManager.register(this)
                onEnable()
                info("Enabled ${javaClass.simpleName}")
            } else if (!value && field) {
                EventManager.unregister(this)
                onDisable()
                info("Disabled ${javaClass.simpleName}")
            }

            field = value
        }

    open fun onEnable() {

    }

    open fun onDisable() {

    }
}