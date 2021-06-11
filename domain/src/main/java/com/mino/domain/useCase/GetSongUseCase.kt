package com.mino.domain.useCase

import com.mino.domain.dto.SongDto

interface GetSongUseCase {
    suspend fun execute(): SongDto
}