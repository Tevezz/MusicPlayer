package com.matheus.musicplayer.domain

import com.matheus.musicplayer.domain.model.Album
import com.matheus.musicplayer.domain.repository.AlbumRepository
import com.matheus.musicplayer.domain.usecase.GetAlbumUseCase
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

internal class GetAlbumUseCaseTest {

    private val repository: AlbumRepository = mockk()
    private lateinit var getAlbumUseCase: GetAlbumUseCase

    @Before
    fun setup() {
        getAlbumUseCase = GetAlbumUseCase(repository)
    }

    @Test
    fun `Get Album Use Case - Success`() = runBlocking {
        val mockAlbum: Album = mockk()
        val collectionID = 1111L
        coEvery { repository.getAlbum(collectionID) } returns mockAlbum
        val response = getAlbumUseCase(collectionID)
        response.shouldBeInstanceOf<Response.Success<Album>>()
        response.data shouldBe mockAlbum
        coVerify(exactly = 1) { repository.getAlbum(collectionID) }
    }

    @Test
    fun `Get Album Use Case - Error`() = runBlocking {
        val collectionID = 1111L
        coEvery { repository.getAlbum(collectionID) } throws Exception("Error!")
        val response = getAlbumUseCase(collectionID)
        response.shouldBeInstanceOf<Response.Error>()
        response.exception shouldNotBe null
        coVerify(exactly = 1) { repository.getAlbum(collectionID) }
    }
}