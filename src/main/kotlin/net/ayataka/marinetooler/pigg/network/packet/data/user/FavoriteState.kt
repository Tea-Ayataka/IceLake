package net.ayataka.marinetooler.pigg.network.packet.data.user

enum class FavoriteState(val state: Byte) {
    STATE_FAVORITE_NONE(0),
    STATE_FAVORITE_ADD(1),
    STATE_FAVORITE_ADDED(2),
    STATE_FAVORITE_MUTUAL(3);

    companion object {
        fun getState(state: Byte) = FavoriteState.values().find { it.state == state }!!
    }
}