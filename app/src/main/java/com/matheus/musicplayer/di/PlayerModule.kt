package com.matheus.musicplayer.di

import android.content.Context
import com.matheus.musicplayer.domain.usecase.GetSongPlayedAfterUseCase
import com.matheus.musicplayer.domain.usecase.GetSongPlayedBeforeUseCase
import com.matheus.musicplayer.domain.usecase.GetSongUseCase
import com.matheus.musicplayer.player.manager.PlayerManager
import com.matheus.musicplayer.player.manager.PlayerManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object PlayerModule {

    @Provides
    @ActivityRetainedScoped
    fun providePlayerManager(
        @ApplicationContext context: Context,
        getSongUseCase: GetSongUseCase,
        getSongPlayedBeforeUseCase: GetSongPlayedBeforeUseCase,
        getSongPlayedAfterUseCase: GetSongPlayedAfterUseCase
    ): PlayerManager = PlayerManagerImpl(
        context,
        getSongUseCase,
        getSongPlayedBeforeUseCase,
        getSongPlayedAfterUseCase
    )

}