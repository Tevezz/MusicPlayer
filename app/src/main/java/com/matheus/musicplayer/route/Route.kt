package com.matheus.musicplayer.route

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


@Serializable
sealed interface Route : NavKey {

    @Serializable
    data object SongList : NavKey

}