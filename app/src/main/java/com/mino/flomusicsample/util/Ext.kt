package com.mino.flomusicsample.util

import com.mino.flomusicsample.vo.LyricsVo

fun String.toTime() = split("\n").map {
    it.substring(1, 10).split(":").let { timeList ->
        (timeList[0].toInt() * 60000) + (timeList[1].toInt() * 1000) + timeList[2].toInt()
    }
}

fun String.toLyrics() = split("\n").map {
    it.substring(11)
}

fun List<String>.toLyricsVoList() = map { LyricsVo(it) }