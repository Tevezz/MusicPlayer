package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.domain.util.Response
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal class GetSongUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var getSongUseCase: GetSongUseCase

    @Before
    fun setup() {
        getSongUseCase = GetSongUseCase(repository)
    }

    @Test
    fun `Get Song Use Case - Success`() = runBlocking {
        val mockSong: Song = mockk()
        val trackId = 1111L
        coEvery { repository.getSong(trackId) } returns mockSong
        val response = getSongUseCase(trackId)
        response.shouldBeInstanceOf<Response.Success<Song>>()
        response.data shouldBe mockSong
        coVerify(exactly = 1) { repository.getSong(trackId) }
    }

    @Test
    fun `Get Song Use Case - Error`() = runBlocking {
        val trackId = 1111L
        val exception = Exception("Error!")
        coEvery { repository.getSong(trackId) } throws exception
        val response = getSongUseCase(trackId)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception shouldBe exception
        coVerify(exactly = 1) { repository.getSong(trackId) }
    }
}