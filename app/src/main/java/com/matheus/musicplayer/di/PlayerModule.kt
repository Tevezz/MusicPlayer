package com.matheus.musicplayer.di

import android.content.Context
import com.matheus.musicplayer.player.manager.PlayerManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun providePlayerManager(@ApplicationContext context: Context): PlayerManager =
        PlayerManager(context)

}