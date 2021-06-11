package com.mino.flomusicsample.useCase

import com.mino.domain.dto.SongDto
import com.mino.domain.repository.SongRepository
import com.mino.domain.useCase.GetSongUseCase
import javax.inject.Inject

class GetSongUseCaseImpl
@Inject
constructor(private val songRepository: SongRepository) : GetSongUseCase {
    override suspend fun execute(): SongDto = songRepository.getSong()
}