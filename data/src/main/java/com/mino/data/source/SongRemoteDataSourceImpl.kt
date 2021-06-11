package com.mino.data.source

import com.mino.data.network.SongApi
import com.mino.data.response.SongResponse
import javax.inject.Inject

class SongRemoteDataSourceImpl
@Inject
constructor(private val songApi: SongApi) : SongRemoteDataSource {
    override suspend fun getSong(): SongResponse = songApi.getSong()
}