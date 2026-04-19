package com.matheus.musicplayer.data.repository

import com.matheus.musicplayer.data.datasource.ITunesAPI
import com.matheus.musicplayer.domain.repository.SongRepository
import javax.inject.Inject

internal class SongRepositoryImpl @Inject constructor(
    private val iTunesAPI: ITunesAPI
) : SongRepository {
}