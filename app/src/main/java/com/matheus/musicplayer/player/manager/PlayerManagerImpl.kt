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
import com.matheus.musicplayer.domain.usecase.GetSongPlayedAfterUseCase
import com.matheus.musicplayer.domain.usecase.GetSongPlayedBeforeUseCase
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.player.service.PlaybackService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlayerManagerImpl(
    context: Context,
    private val getSongUseCase: GetSongUseCase,
    private val getSongPlayedBeforeUseCase: GetSongPlayedBeforeUseCase,
    private val getSongPlayedAfterUseCase: GetSongPlayedAfterUseCase
) : PlayerManager {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val _state = MutableStateFlow(PlaybackState())
    override val playbackState: StateFlow<PlaybackState> = _state.asStateFlow()

    private var mediaController: MediaController? = null
    private var olderSong: Song? = null
    private var newerSong: Song? = null
    private var currentUrl: String? = null

    init {
        val token = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val future = MediaController.Builder(context, token).buildAsync()
        future.addListener({
            val controller = future.get()
            mediaController = controller
            controller.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    _state.update {
                        it.copy(
                            controls = it.controls.copy(
                                isPlaying = isPlaying
                            )
                        )
                    }
                    handlePositionJob(isPlaying)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    val duration = controller.duration.takeIf { it > 0 } ?: 30_000L
                    _state.update {
                        it.copy(
                            position = it.position.copy(
                                duration = duration
                            )
                        )
                    }
                    if (playbackState == Player.STATE_ENDED) onNext()
                }
            })
        }, MoreExecutors.directExecutor())
    }

    private var positionLoopJob: Job? = null
    private fun handlePositionJob(isPlaying: Boolean) {
        if (isPlaying) {
            positionLoopJob = scope.launch(Dispatchers.Main) {
                while (true) {
                    _state.update {
                        it.copy(
                            position = it.position.copy(
                                position = mediaController?.currentPosition ?: 0L
                            )
                        )
                    }
                    delay(100)
                }
            }
        } else {
            positionLoopJob?.cancel()
            positionLoopJob = null
        }
    }

    override fun loadAndPlay(trackId: Long) {
        scope.launch {
            val song = getSongUseCase(trackId).resultOrNull() ?: return@launch
            olderSong = null
            newerSong = null
            val isSameSong = _state.value.song.song?.trackId == song.trackId
            _state.update {
                it.copy(
                    song = SongState(song),
                    controls = it.controls.copy(
                        hasNext = false,
                        hasPrevious = false,
                        isRepeating = if (isSameSong) it.controls.isRepeating else false
                    ),
                    position = PositionState()
                )
            }
            play(song)

            // This is to load in parallel
            coroutineScope {
                val nextSong = async { getSongPlayedBeforeUseCase(trackId) }
                val previousSong = async { getSongPlayedAfterUseCase(trackId) }
                olderSong = nextSong.await().resultOrNull()
                newerSong = previousSong.await().resultOrNull()
            }
            _state.update {
                it.copy(
                    controls = it.controls.copy(
                        hasNext = olderSong != null,
                        hasPrevious = newerSong != null
                    )
                )
            }
        }
    }

    override fun onNext() {
        val song = olderSong ?: return
        loadAndPlay(song.trackId)
    }

    override fun onPrevious() {
        val song = newerSong ?: return
        loadAndPlay(song.trackId)
    }

    override fun onPlayPause() {
        if (_state.value.controls.isPlaying) pause()
        else {
            val song = _state.value.song.song ?: return
            play(song)
        }
    }

    private fun play(song: Song) {
        val url = song.previewUrl ?: return

        scope.launch {
            while (mediaController == null) {
                delay(100)
            }

            withContext(Dispatchers.Main) {
                mediaController?.also { controller ->
                    if (currentUrl == url) {
                        if (controller.playbackState == Player.STATE_ENDED) controller.seekTo(0)
                        controller.play()
                        return@also
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
            }
        }
    }

    private fun pause() {
        mediaController?.pause()
    }

    override fun seekTo(position: Long) {
        mediaController?.seekTo(position)
    }

    override fun setRepeatMode(enabled: Boolean) {
        mediaController?.repeatMode =
            if (enabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
        _state.update {
            it.copy(
                controls = it.controls.copy(
                    isRepeating = enabled
                )
            )
        }
    }
}
