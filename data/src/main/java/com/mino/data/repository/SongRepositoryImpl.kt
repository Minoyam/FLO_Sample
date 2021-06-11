package com.mino.data.repository

import com.mino.data.source.SongRemoteDataSource
import com.mino.domain.dto.SongDto
import com.mino.domain.repository.SongRepository
import javax.inject.Inject

class SongRepositoryImpl
@Inject
constructor(private val songRemoteDataSource: SongRemoteDataSource) : SongRepository {
    override suspend fun getSong(): SongDto = songRemoteDataSource.getSong().toDomain()
}