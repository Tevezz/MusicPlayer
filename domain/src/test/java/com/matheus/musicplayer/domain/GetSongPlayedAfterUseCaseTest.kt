package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetSongPlayedAfterUseCase
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
        val response = getSongPlayedAfterUseCase(trackId)
        response.shouldBeInstanceOf<Response.Success<Song>>()
        response.data shouldBe mockSong
        coVerify(exactly = 1) { repository.getSongPlayedAfter(trackId) }
    }

    @Test
    fun `Get Song Played After Use Case - Returns Null When No Newer Song Exists`() = runBlocking {
        val trackId = 1111L
        coEvery { repository.getSongPlayedAfter(trackId) } returns null
        val response = getSongPlayedAfterUseCase(trackId)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception.message shouldBe "No song was found to play after the current track."
        coVerify(exactly = 1) { repository.getSongPlayedAfter(trackId) }
    }

    @Test
    fun `Get Song Played After Use Case - Error`() = runBlocking {
        val trackId = 1111L
        val exception = Exception("Error!")
        coEvery { repository.getSongPlayedAfter(trackId) } throws exception
        val response = getSongPlayedAfterUseCase(trackId)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception shouldNotBe null
        coVerify(exactly = 1) { repository.getSongPlayedAfter(trackId) }
    }
}
