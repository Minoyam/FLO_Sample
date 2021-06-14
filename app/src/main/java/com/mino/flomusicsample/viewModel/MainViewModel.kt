package com.mino.flomusicsample.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mino.domain.useCase.GetSongUseCase
import com.mino.flomusicsample.util.Event
import com.mino.flomusicsample.util.toApp
import com.mino.flomusicsample.util.toLyrics
import com.mino.flomusicsample.util.toTime
import com.mino.flomusicsample.vo.SongVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(private val getSongUseCase: GetSongUseCase) : ViewModel() {

    private val _songVo = MutableLiveData<Event<SongVo>>()
    val songVo: LiveData<Event<SongVo>> get() = _songVo

    private val _lyricsList = MutableLiveData<Event<List<String>>>()
    val lyricsList: LiveData<Event<List<String>>> get() = _lyricsList

    private val _currentPosition = MutableLiveData<Event<Int>>()
    val currentPosition: LiveData<Event<Int>> get() = _currentPosition

    private val _lyricsTimeList = MutableLiveData<Event<List<Int>>>()
    val lyricsTimeList: LiveData<Event<List<Int>>> get() = _lyricsTimeList

    private val _lyricsTimeFirst = MutableLiveData<Event<Int>>()
    val lyricsTimeFirst: LiveData<Event<Int>> get() = _lyricsTimeFirst

    private val _lyricsTimeSecond = MutableLiveData<Event<Int>>()
    val lyricsTimeSecond: LiveData<Event<Int>> get() = _lyricsTimeSecond

    private val _lyricsFirst = MutableLiveData<Event<String>>()
    val lyricsFirst: LiveData<Event<String>> get() = _lyricsFirst

    private val _lyricsSecond = MutableLiveData<Event<String>>()
    val lyricsSecond: LiveData<Event<String>> get() = _lyricsSecond

    val isMove = MutableLiveData<Event<Boolean>>()

    fun setCurrentPosition(position: Int, isMove: Boolean = false) {
        if (isMove) {
            this.isMove.value = Event(isMove)
        }
        _currentPosition.value = Event(position)
    }

    fun getSong() {
        viewModelScope.launch {
            getSongUseCase.execute().toApp().let {
                _songVo.value = Event(it)
                setLyrics(it.lyrics)
            }
        }
    }

    fun setCurrentLyrics(currentPosition: Int): Boolean {
        lyricsTimeSecond.value?.peekContent()?.let { timeSecond ->
            lyricsTimeList.value?.peekContent()?.let { timeList ->
                if (currentPosition >= timeSecond && timeSecond != 0) {
                    _lyricsTimeFirst.value = Event(timeSecond)
                    _lyricsFirst.value = lyricsSecond.value
                    val index = timeList.indexOf(timeSecond)

                    _lyricsTimeSecond.value =
                        if (index != timeList.size - 1)
                            Event(timeList[index + 1])
                        else Event(0)

                    lyricsList.value?.peekContent()?.let {
                        _lyricsSecond.value = if (index != timeList.size - 1)
                            Event(it[index + 1])
                        else Event("")
                    }
                    return true
                }
            }
        }
        return false
    }

    fun moveSeek(movePosition: Int) {
        lyricsTimeList.value?.peekContent()?.let { time ->
            lyricsList.value?.peekContent()?.let { lyrics ->
                time.forEachIndexed { index, i ->
                    if (i <= movePosition) {
                        _lyricsFirst.value = Event(lyrics[index])
                        _lyricsTimeFirst.value = Event(time[index])
                        if (index != time.size - 1) {
                            _lyricsSecond.value = Event(lyrics[index + 1])
                            _lyricsTimeSecond.value = Event(time[index + 1])
                        } else {
                            _lyricsSecond.value = Event("")
                            _lyricsTimeSecond.value = Event(0)
                        }
                        return@forEachIndexed
                    }
                }
            }
        }
    }

    private fun setLyrics(lyrics: String) {
        lyrics.toTime().let {
            _lyricsTimeList.value = Event(it)
            _lyricsTimeFirst.value = Event(it[0])
            _lyricsTimeSecond.value = Event(it[1])
        }
        lyrics.toLyrics().let {
            _lyricsList.value = Event(it)
            _lyricsFirst.value = Event(it[0])
            _lyricsSecond.value = Event(it[1])
        }
    }
}