package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.SaveRecentlyPlayedUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class SaveRecentlyPlayedUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var saveRecentlyPlayedUseCase: SaveRecentlyPlayedUseCase

    @Before
    fun setup() {
        saveRecentlyPlayedUseCase = SaveRecentlyPlayedUseCase(repository)
    }

    @Test
    fun `Save Recently Played Use Case - Success`() = runBlocking {
        val mockSong: Song = mockk()
        coJustRun { repository.saveRecentlyPlayed(mockSong) }
        saveRecentlyPlayedUseCase(mockSong)
        coVerify(exactly = 1) { repository.saveRecentlyPlayed(mockSong) }
    }

    @Test
    fun `Save Recently Played Use Case - Error`() = runBlocking {
        val mockSong: Song = mockk()
        val exception = Exception("Error!")
        coEvery { repository.saveRecentlyPlayed(mockSong) } throws exception
        val thrown = shouldThrow<Exception> { saveRecentlyPlayedUseCase(mockSong) }
        thrown.message shouldBe exception.message
        coVerify(exactly = 1) { repository.saveRecentlyPlayed(mockSong) }
    }
}