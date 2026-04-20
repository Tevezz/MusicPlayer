package com.matheus.musicplayer.player.manager

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

class PlayerManager(context: Context) {

    private val player = ExoPlayer.Builder(context).build()
    private var currentUrl: String? = null

    fun play(url: String) {
        if (currentUrl == url) {
            if (player.playbackState == Player.STATE_ENDED) {
                player.seekTo(0)
            }
            player.play()
            return
        }

        currentUrl = url
        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    fun pause() {
        player.pause()
    }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun getPlayer(): ExoPlayer = player

    fun reset() {
        player.stop()
        player.clearMediaItems()
        currentUrl = null
    }
}