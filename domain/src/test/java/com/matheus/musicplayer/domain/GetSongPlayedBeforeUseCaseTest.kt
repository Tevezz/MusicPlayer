package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetSongPlayedBeforeUseCase
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetSongPlayedBeforeUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var getSongPlayedBeforeUseCase: GetSongPlayedBeforeUseCase

    @Before
    fun setup() {
        getSongPlayedBeforeUseCase = GetSongPlayedBeforeUseCase(repository)
    }

    @Test
    fun `Get Song Played Before Use Case - Returns Song`() = runBlocking {
        val mockSong: Song = mockk()
        val trackId = 1111L
        coEvery { repository.getSongPlayedBefore(trackId) } returns mockSong
        val result = getSongPlayedBeforeUseCase(trackId)
        result shouldBe mockSong
        coVerify(exactly = 1) { repository.getSongPlayedBefore(trackId) }
    }

    @Test
    fun `Get Song Played Before Use Case - Returns Null When No Older Song Exists`() = runBlocking {
        val trackId = 1111L
        coEvery { repository.getSongPlayedBefore(trackId) } returns null
        val result = getSongPlayedBeforeUseCase(trackId)
        result shouldBe null
        coVerify(exactly = 1) { repository.getSongPlayedBefore(trackId) }
    }

    @Test
    fun `Get Song Played Before Use Case - Error`() = runBlocking {
        val trackId = 1111L
        val exception = Exception("Error!")
        coEvery { repository.getSongPlayedBefore(trackId) } throws exception
        val thrown = shouldThrow<Exception> { getSongPlayedBeforeUseCase(trackId) }
        thrown.message shouldBe exception.message
        coVerify(exactly = 1) { repository.getSongPlayedBefore(trackId) }
    }
}
