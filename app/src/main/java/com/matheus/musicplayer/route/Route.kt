package com.matheus.musicplayer.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object SongList : NavKey

    @Serializable
    data class Player(
        val trackId: Long
    ) : NavKey

    @Serializable
    data object Album : NavKey
}