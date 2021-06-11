package com.mino.flomusicsample.di

import com.mino.domain.useCase.GetSongUseCase
import com.mino.flomusicsample.useCase.GetSongUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class UseCaseModule {

    @Singleton
    @Binds
    abstract fun bindGetSongUseCase(impl: GetSongUseCaseImpl): GetSongUseCase
}