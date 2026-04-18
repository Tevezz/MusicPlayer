package com.matheus.musicplayer.data.di

import com.matheus.musicplayer.data.repository.SongRepositoryImpl
import com.matheus.musicplayer.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryModule {

    @Binds
    internal abstract fun bindSongRepository(
        impl: SongRepositoryImpl
    ): SongRepository

}