package com.mino.data.response


import com.google.gson.annotations.SerializedName
import com.mino.domain.dto.SongDto

data class SongResponse(
    @SerializedName("album")
    val album: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("file")
    val `file`: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("lyrics")
    val lyrics: String,
    @SerializedName("singer")
    val singer: String,
    @SerializedName("title")
    val title: String
) {
    fun toDomain() = SongDto(album, duration, file, image, lyrics, singer, title)
}