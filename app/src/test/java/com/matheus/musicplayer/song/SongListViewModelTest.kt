package com.matheus.musicplayer.song

import androidx.paging.PagingData
import app.cash.turbine.test
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.usecase.GetRecentlyPlayedUseCase
import com.matheus.musicplayer.domain.usecase.SaveRecentlyPlayedUseCase
import com.matheus.musicplayer.domain.usecase.SearchSongsUseCase
import com.matheus.musicplayer.domain.util.Response
import com.matheus.musicplayer.player.manager.PlaybackState
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.song.viewmodel.SongListEvent
import com.matheus.musicplayer.song.viewmodel.SongListViewModel
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SongListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val playerManager: PlayerManager = mockk(relaxed = true)
    private val searchSongsUseCase: SearchSongsUseCase = mockk()
    private val getRecentlyPlayedUseCase: GetRecentlyPlayedUseCase = mockk()
    private val saveRecentlyPlayedUseCase: SaveRecentlyPlayedUseCase = mockk()

    private lateinit var viewModel: SongListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { playerManager.playbackState } returns MutableStateFlow(PlaybackState())
        every { getRecentlyPlayedUseCase() } returns flowOf(PagingData.Companion.empty())
        every { searchSongsUseCase(any()) } returns flowOf(PagingData.Companion.empty())
        viewModel = SongListViewModel(
            playerManager,
            searchSongsUseCase,
            getRecentlyPlayedUseCase,
            saveRecentlyPlayedUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildSong(trackId: Long = 1L) = Song(
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
    fun `SearchQuery - Initial Value Is Empty`() = runTest(testDispatcher) {
        viewModel.searchQuery.value shouldBe ""
    }

    @Test
    fun `OnSearchChange - Updates SearchQuery`() = runTest(testDispatcher) {
        viewModel.onSearchChange("rock")

        viewModel.searchQuery.value shouldBe "rock"
    }

    @Test
    fun `Songs Flow - Blank Query Calls GetRecentlyPlayedUseCase After Debounce`() =
        runTest(testDispatcher) {
            val job = launch { viewModel.songs.collect { _ -> } }
            advanceTimeBy(600)

            verify(exactly = 1) { getRecentlyPlayedUseCase() }
            verify(exactly = 0) { searchSongsUseCase(any()) }

            job.cancel()
        }

    @Test
    fun `Songs Flow - Non-Blank Query Calls SearchSongsUseCase After Debounce`() =
        runTest(testDispatcher) {
            val job = launch { viewModel.songs.collect { _ -> } }
            advanceTimeBy(600)

            viewModel.onSearchChange("Beatles")
            advanceTimeBy(600)

            verify(exactly = 1) { searchSongsUseCase("Beatles") }

            job.cancel()
        }

    @Test
    fun `Songs Flow - Debounce Prevents Intermediate Queries From Triggering Searches`() =
        runTest(testDispatcher) {
            val job = launch { viewModel.songs.collect { _ -> } }
            advanceTimeBy(600)

            viewModel.onSearchChange("B")
            advanceTimeBy(100)
            viewModel.onSearchChange("Be")
            advanceTimeBy(100)
            viewModel.onSearchChange("Beatles")
            advanceTimeBy(600)

            verify(exactly = 0) { searchSongsUseCase("B") }
            verify(exactly = 0) { searchSongsUseCase("Be") }
            verify(exactly = 1) { searchSongsUseCase("Beatles") }

            job.cancel()
        }

    @Test
    fun `Songs Flow - Same Query Twice Does Not Trigger Duplicate Search`() =
        runTest(testDispatcher) {
            val job = launch { viewModel.songs.collect { _ -> } }
            advanceTimeBy(600)

            viewModel.onSearchChange("Beatles")
            advanceTimeBy(600)
            viewModel.onSearchChange("Beatles")
            advanceTimeBy(600)

            verify(exactly = 1) { searchSongsUseCase("Beatles") }

            job.cancel()
        }

    @Test
    fun `Songs Flow - Clearing Search Query Calls GetRecentlyPlayedUseCase Again`() =
        runTest(testDispatcher) {
            val job = launch { viewModel.songs.collect { _ -> } }
            advanceTimeBy(600)

            viewModel.onSearchChange("Beatles")
            advanceTimeBy(600)

            viewModel.onSearchChange("")
            advanceTimeBy(600)

            verify(exactly = 2) { getRecentlyPlayedUseCase() }

            job.cancel()
        }

    @Test
    fun `OnSongClick - Emits NavToPlayer Event With Correct TrackId`() = runTest(testDispatcher) {
        val song = buildSong(trackId = 42L)
        coEvery { saveRecentlyPlayedUseCase(song) } returns Response.Success(Unit)

        viewModel.events.test {
            viewModel.onSongClick(song)
            advanceTimeBy(100)

            val event = awaitItem()
            event.shouldBeInstanceOf<SongListEvent.NavToPlayer>()
            event.trackId shouldBe 42L

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `OnSongClick - Saves Song As Recently Played`() = runTest(testDispatcher) {
        val song = buildSong(trackId = 42L)
        coEvery { saveRecentlyPlayedUseCase(song) } returns Response.Success(Unit)

        viewModel.onSongClick(song)
        advanceTimeBy(100)

        coVerify(exactly = 1) { saveRecentlyPlayedUseCase(song) }
    }

    @Test
    fun `OnSongClick - Does Not Emit NavToPlayer Event When Save Fails`() =
        runTest(testDispatcher) {
            val song = buildSong(trackId = 42L)
            coEvery { saveRecentlyPlayedUseCase(song) } returns Response.Error(Exception())

            viewModel.events.test {
                viewModel.onSongClick(song)
                advanceTimeBy(100)

                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `OnAlbumClick - Emits NavToAlbum Event With Correct TrackId`() = runTest(testDispatcher) {
        val song = buildSong(trackId = 99L)

        viewModel.events.test {
            viewModel.onAlbumClick(song)
            advanceTimeBy(100)

            val event = awaitItem()
            event.shouldBeInstanceOf<SongListEvent.NavToAlbum>()
            event.trackId shouldBe 99L

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `OnMiniPlayerClick - Emits NavToPlayer Event With Correct TrackId`() =
        runTest(testDispatcher) {
            val song = buildSong(trackId = 55L)

            viewModel.events.test {
                viewModel.onMiniPlayerClick(song)
                advanceTimeBy(100)

                val event = awaitItem()
                event.shouldBeInstanceOf<SongListEvent.NavToPlayer>()
                event.trackId shouldBe 55L

                cancelAndConsumeRemainingEvents()
            }
        }
}