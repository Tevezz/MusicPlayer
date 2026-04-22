package com.matheus.musicplayer.player.manager

import android.content.ComponentName
import android.content.Context
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.player.service.PlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerManager(context: Context) {

    private val _controllerFlow = MutableStateFlow<MediaController?>(null)
    val controllerFlow: StateFlow<MediaController?> = _controllerFlow.asStateFlow()

    private var currentUrl: String? = null
    private var pendingSong: Song? = null

    init {
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener({
            _controllerFlow.value = future.get()
            pendingSong?.let { song ->
                pendingSong = null
                play(song)
            }
        }, MoreExecutors.directExecutor())
    }

    fun play(song: Song) {
        val url = song.previewUrl ?: return
        val controller = _controllerFlow.value ?: run {
            pendingSong = song
            return
        }
        pendingSong = null
        if (currentUrl == url) {
            if (controller.playbackState == Player.STATE_ENDED) controller.seekTo(0)
            controller.play()
            return
        }
        currentUrl = url
        controller.repeatMode = Player.REPEAT_MODE_OFF
        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(song.trackName)
                    .setArtist(song.artistName)
                    .setArtworkUri(song.artworkUrl100?.toUri())
                    .build()
            )
            .build()
        controller.setMediaItem(mediaItem)
        controller.prepare()
        controller.play()
    }

    fun pause() {
        _controllerFlow.value?.pause()
    }

    fun seekTo(position: Long) {
        _controllerFlow.value?.seekTo(position)
    }

    fun setRepeatMode(enabled: Boolean) {
        _controllerFlow.value?.repeatMode =
            if (enabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
    }
}
