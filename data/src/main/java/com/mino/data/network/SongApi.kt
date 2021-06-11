package com.mino.data.network

import com.mino.data.response.SongResponse
import retrofit2.http.GET

interface SongApi {

    @GET("/2020-flo/song.json")
    suspend fun getSong(): SongResponse

    companion object {
        const val BASE_URL = "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com"
    }
}