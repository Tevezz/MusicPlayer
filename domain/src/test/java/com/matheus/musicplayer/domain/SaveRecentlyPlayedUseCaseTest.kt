package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.SaveRecentlyPlayedUseCase
import com.matheus.musicplayer.domain.util.Response
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
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
        coEvery { repository.saveRecentlyPlayed(mockSong) } returns Unit
        val response = saveRecentlyPlayedUseCase(mockSong)
        response.shouldBeInstanceOf<Response.Success<Unit>>()
        response.data shouldBe Unit
        coVerify(exactly = 1) { repository.saveRecentlyPlayed(mockSong) }
    }

    @Test
    fun `Save Recently Played Use Case - Error`() = runBlocking {
        val mockSong: Song = mockk()
        val exception = Exception("Error!")
        coEvery { repository.saveRecentlyPlayed(mockSong) } throws exception
        val response = saveRecentlyPlayedUseCase(mockSong)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception shouldBe exception
        coVerify(exactly = 1) { repository.saveRecentlyPlayed(mockSong) }
    }
}