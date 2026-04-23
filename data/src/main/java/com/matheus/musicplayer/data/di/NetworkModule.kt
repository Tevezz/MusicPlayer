package com.matheus.musicplayer.data.di

import com.matheus.musicplayer.data.datasource.remote.ITunesAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val ITUNES_URL = "https://itunes.apple.com/"

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(ITUNES_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideITunesApi(retrofit: Retrofit): ITunesAPI =
        retrofit.create(ITunesAPI::class.java)
}