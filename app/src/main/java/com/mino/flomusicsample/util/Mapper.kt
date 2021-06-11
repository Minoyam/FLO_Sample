package com.mino.flomusicsample.util

import com.mino.domain.dto.SongDto
import com.mino.flomusicsample.vo.SongVo

fun SongDto.toApp() = SongVo(album, duration, file, image, lyrics, singer, title)