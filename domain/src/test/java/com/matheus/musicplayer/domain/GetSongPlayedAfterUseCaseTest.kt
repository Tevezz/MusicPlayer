package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetSongPlayedAfterUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetSongPlayedAfterUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var getSongPlayedAfterUseCase: GetSongPlayedAfterUseCase

    @Before
    fun setup() {
        getSongPlayedAfterUseCase = GetSongPlayedAfterUseCase(repository)
    }

    @Test
    fun `Get Song Played After Use Case - Returns Song`() = runBlocking {
        val mockSong: Song = mockk()
        val trackId = 1111L
        coEvery { repository.getSongPlayedAfter(trackId) } returns mockSong
        val result = getSongPlayedAfterUseCase(trackId)
        result shouldBe mockSong
        coVerify(exactly = 1) { repository.getSongPlayedAfter(trackId) }
    }

    @Test
    fun `Get Song Played After Use Case - Returns Null When No Newer Song Exists`() = runBlocking {
        val trackId = 1111L
        coEvery { repository.getSongPlayedAfter(trackId) } returns null
        val result = getSongPlayedAfterUseCase(trackId)
        result shouldBe null
        coVerify(exactly = 1) { repository.getSongPlayedAfter(trackId) }
    }

    @Test
    fun `Get Song Played After Use Case - Error`() = runBlocking {
        val trackId = 1111L
        val exception = Exception("Error!")
        coEvery { repository.getSongPlayedAfter(trackId) } throws exception
        val thrown = shouldThrow<Exception> { getSongPlayedAfterUseCase(trackId) }
        thrown.message shouldBe exception.message
        coVerify(exactly = 1) { repository.getSongPlayedAfter(trackId) }
    }
}
