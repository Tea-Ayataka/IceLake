package net.ayataka.marinetooler.emulator

import net.ayataka.marinetooler.emulator.pigg.VirtualPigg

object Emulator {
    private val vPiggs = mutableListOf<VirtualPigg>()

    fun add(amebaId: String, password: String) {
        // 重複チェック
        if (vPiggs.any { it.amebaId == amebaId }) {
            return
        }

        vPiggs.add(VirtualPigg(amebaId, password))
    }

    fun remove(amebaId: String) {
        val pigg = vPiggs.find { it.amebaId == amebaId }
        pigg?.disconnect()
        vPiggs.remove(pigg)
    }

    fun login() {

    }

    fun connect() {
        vPiggs.forEach { it.connect() }
    }

    fun getActives(): List<VirtualPigg> {
        return vPiggs.filter { it.connected }
    }
}