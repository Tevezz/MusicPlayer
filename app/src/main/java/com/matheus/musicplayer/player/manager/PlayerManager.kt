package com.matheus.musicplayer.player.manager

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.matheus.musicplayer.player.service.PlaybackService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerManager(context: Context) {

    private val _controllerFlow = MutableStateFlow<MediaController?>(null)
    val controllerFlow: StateFlow<MediaController?> = _controllerFlow.asStateFlow()

    private var currentUrl: String? = null
    private var pendingUrl: String? = null

    init {
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener({
            _controllerFlow.value = future.get()
            pendingUrl?.let { url ->
                pendingUrl = null
                play(url)
            }
        }, MoreExecutors.directExecutor())
    }

    fun play(url: String) {
        val ctrl = _controllerFlow.value ?: run {
            pendingUrl = url
            return
        }
        if (currentUrl == url) {
            if (ctrl.playbackState == Player.STATE_ENDED) ctrl.seekTo(0)
            ctrl.play()
            return
        }
        currentUrl = url
        ctrl.repeatMode = Player.REPEAT_MODE_OFF
        ctrl.setMediaItem(MediaItem.fromUri(url))
        ctrl.prepare()
        ctrl.play()
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
