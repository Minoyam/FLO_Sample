package com.mino.data.source

import com.mino.data.response.SongResponse

interface SongRemoteDataSource {
    suspend fun getSong(): SongResponse
}