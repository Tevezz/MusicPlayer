package com.matheus.musicplayer.player

import app.cash.turbine.test
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.player.manager.ControlsState
import com.matheus.musicplayer.player.manager.PlaybackState
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.player.viewmodel.PlayerEvent
import com.matheus.musicplayer.player.viewmodel.PlayerViewModel
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class PlayerViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val playerManager: PlayerManager = mockk(relaxed = true)

    private lateinit var viewModel: PlayerViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { playerManager.playbackState } returns MutableStateFlow(PlaybackState())
        viewModel = PlayerViewModel(playerManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildSong(trackId: Long) = Song(
        trackId = trackId,
        trackName = "Track Name",
        artistName = "Artist Name",
        collectionId = null,
        collectionName = null,
        artworkUrl100 = null,
        previewUrl = null,
        trackTimeMillis = null
    )

    @Test
    fun `OnRepeatClick - When Not Repeating, Calls SetRepeatMode With True`() =
        runTest(testDispatcher) {
            every { playerManager.playbackState } returns MutableStateFlow(
                PlaybackState(controls = ControlsState(isRepeating = false))
            )
            viewModel = PlayerViewModel(playerManager)

            viewModel.onRepeatClick()

            verify(exactly = 1) { playerManager.setRepeatMode(true) }
        }

    @Test
    fun `OnRepeatClick - When Repeating, Calls SetRepeatMode With False`() =
        runTest(testDispatcher) {
            every { playerManager.playbackState } returns MutableStateFlow(
                PlaybackState(controls = ControlsState(isRepeating = true))
            )
            viewModel = PlayerViewModel(playerManager)

            viewModel.onRepeatClick()

            verify(exactly = 1) { playerManager.setRepeatMode(false) }
        }

    @Test
    fun `OnAlbumClick - Emits NavToAlbum Event With Correct TrackId`() = runTest(testDispatcher) {
        val song = buildSong(trackId = 7L)

        viewModel.events.test {
            viewModel.onAlbumClick(song)
            advanceTimeBy(100)

            val event = awaitItem()
            event.shouldBeInstanceOf<PlayerEvent.NavToAlbum>()
            event.trackId shouldBe 7L

            cancelAndConsumeRemainingEvents()
        }
    }
}
