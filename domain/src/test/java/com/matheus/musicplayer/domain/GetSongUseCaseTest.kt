package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.kotest.matchers.shouldBe
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
        val result = getSongUseCase(trackId)
        result.shouldBeSuccess()
        result.getOrNull() shouldBe mockSong
        coVerify(exactly = 1) { repository.getSong(trackId) }
    }

    @Test
    fun `Get Song Use Case - Error`() = runBlocking {
        val trackId = 1111L
        val exception = Exception("Error!")
        coEvery { repository.getSong(trackId) } throws exception
        val result = getSongUseCase(trackId)
        result.shouldBeFailure()
        result.exceptionOrNull() shouldBe exception
        coVerify(exactly = 1) { repository.getSong(trackId) }
    }
}