package com.matheus.musicplayer.domain

import androidx.paging.PagingData
import com.matheus.musicplayer.domain.model.Song
import com.matheus.musicplayer.domain.repository.SongRepository
import com.matheus.musicplayer.domain.usecase.SearchSongsUseCase
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.Flow
import org.junit.Before
import org.junit.Test

internal class SearchSongsUseCaseTest {

    private val repository: SongRepository = mockk()
    private lateinit var searchSongsUseCase: SearchSongsUseCase

    @Before
    fun setup() {
        searchSongsUseCase = SearchSongsUseCase(repository)
    }

    @Test
    fun `Search Songs Use Case - returns flow from repository`() {
        val query = "Beatles"
        val mockFlow: Flow<PagingData<Song>> = mockk()
        every { repository.searchSongs(query) } returns mockFlow
        val result = searchSongsUseCase(query)
        result shouldBe mockFlow
        verify(exactly = 1) { repository.searchSongs(query) }
    }

    @Test
    fun `Search Songs Use Case - empty query returns flow from repository`() {
        val query = ""
        val mockFlow: Flow<PagingData<Song>> = mockk()
        every { repository.searchSongs(query) } returns mockFlow
        val result = searchSongsUseCase(query)
        result shouldBe mockFlow
        verify(exactly = 1) { repository.searchSongs(query) }
    }
}
