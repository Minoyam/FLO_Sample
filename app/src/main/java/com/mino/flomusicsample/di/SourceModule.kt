package com.mino.flomusicsample.di

import com.mino.data.source.SongRemoteDataSource
import com.mino.data.source.SongRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
abstract class SourceModule {

    @Singleton
    @Binds
    abstract fun bindSongRemoteSource(impl: SongRemoteDataSourceImpl): SongRemoteDataSource
}