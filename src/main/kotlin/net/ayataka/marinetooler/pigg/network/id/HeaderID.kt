package net.ayataka.marinetooler.pigg.network.id

enum class HeaderID(val id: Short) {
    COMMAND(0x10),
    CIPHER_KEY(0x1F0)
}