package net.ayataka.marinetooler.pigg.network.packet.data.area

enum class Wall(val north: Byte, val east: Byte, val south: Byte, val west: Byte) {
    NONE(0, 0, 0, 0),
    W(0, 0, 0, 1),
    S(0, 0, 1, 0),
    SW(0, 0, 1, 1),
    E(0, 1, 0, 0),
    EW(0, 1, 0, 1),
    ES(0, 1, 1, 0),
    ESW(0, 1, 1, 1),
    N(1, 0, 0, 0),
    NW(1, 0, 0, 1),
    NS(1, 0, 1, 0),
    NSW(1, 0, 1, 1),
    NE(1, 1, 0, 0),
    NEW(1, 1, 0, 1),
    NES(1, 1, 1, 0),
    NESW(1, 1, 1, 1)
}
