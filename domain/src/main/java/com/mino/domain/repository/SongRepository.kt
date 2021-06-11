package com.mino.domain.repository

import com.mino.domain.dto.SongDto

interface SongRepository {
    suspend fun getSong(): SongDto
}