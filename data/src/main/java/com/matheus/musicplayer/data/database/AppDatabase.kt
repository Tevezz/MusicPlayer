package com.matheus.musicplayer.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matheus.musicplayer.data.datasource.SongDao
import com.matheus.musicplayer.data.model.local.SongEntity

@Database(entities = [SongEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
}