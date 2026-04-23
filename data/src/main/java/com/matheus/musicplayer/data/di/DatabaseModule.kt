package com.matheus.musicplayer.data.di

import android.content.Context
import androidx.room.Room
import com.matheus.musicplayer.data.database.AppDatabase
import com.matheus.musicplayer.data.datasource.local.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val MUSIC_PLAYER_DB = "music_player_db"

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            MUSIC_PLAYER_DB
        ).fallbackToDestructiveMigration(true).build()
    }

    @Provides
    fun providesSongDao(db: AppDatabase): SongDao = db.songDao()
}