package com.matheus.musicplayer.domain

import androidx.paging.PagingData
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.GetRecentlyPlayedUseCase
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Test

internal class GetRecentlyPlayedUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var getRecentlyPlayedUseCase: GetRecentlyPlayedUseCase

    @Before
    fun setup() {
        getRecentlyPlayedUseCase = GetRecentlyPlayedUseCase(repository)
    }

    @Test
    fun `Get Recently Played Use Case - Flow of Paging Data`() {
        val mockFlow: Flow<PagingData<Song>> = mockk()
        every { repository.getRecentlyPlayed() } returns mockFlow
        val result = getRecentlyPlayedUseCase()
        result shouldBe mockFlow
        verify(exactly = 1) { repository.getRecentlyPlayed() }
    }
}
