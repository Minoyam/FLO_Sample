package com.mino.domain.dto

data class SongDto(
    val album: String,
    val duration: Int,
    val `file`: String,
    val image: String,
    val lyrics: String,
    val singer: String,
    val title: String
)