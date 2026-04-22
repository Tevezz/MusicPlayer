package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetSongPlayedBeforeUseCase
import com.matheus.musicplayer.domain.util.Response
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
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
        val response = getSongPlayedBeforeUseCase(trackId)
        response.shouldBeInstanceOf<Response.Success<Song>>()
        response.data shouldBe mockSong
        coVerify(exactly = 1) { repository.getSongPlayedBefore(trackId) }
    }

    @Test
    fun `Get Song Played Before Use Case - Returns Null When No Older Song Exists`() = runBlocking {
        val trackId = 1111L
        coEvery { repository.getSongPlayedBefore(trackId) } returns null
        val response = getSongPlayedBeforeUseCase(trackId)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception.message shouldBe "No song was found played before the current track."
        coVerify(exactly = 1) { repository.getSongPlayedBefore(trackId) }
    }

    @Test
    fun `Get Song Played Before Use Case - Error`() = runBlocking {
        val trackId = 1111L
        val exception = Exception("Error!")
        coEvery { repository.getSongPlayedBefore(trackId) } throws exception
        val response = getSongPlayedBeforeUseCase(trackId)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception shouldNotBe null
        coVerify(exactly = 1) { repository.getSongPlayedBefore(trackId) }
    }
}
