package com.mino.flomusicsample.di

import com.mino.data.repository.SongRepositoryImpl
import com.mino.domain.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindSongRepository(impl: SongRepositoryImpl): SongRepository
}