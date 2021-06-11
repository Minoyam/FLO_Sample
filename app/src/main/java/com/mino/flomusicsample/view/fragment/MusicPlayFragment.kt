package com.mino.flomusicsample.view.fragment

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.mino.flomusicsample.R
import com.mino.flomusicsample.base.BaseFragment
import com.mino.flomusicsample.databinding.FragmentMusicPlayBinding
import com.mino.flomusicsample.util.Event
import com.mino.flomusicsample.util.EventObserver
import com.mino.flomusicsample.viewModel.MainViewModel
import com.mino.flomusicsample.vo.SongVo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MusicPlayFragment : BaseFragment<FragmentMusicPlayBinding>(R.layout.fragment_music_play) {

    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private lateinit var job: Job

    private val viewModel: MainViewModel by activityViewModels()
    private val scope: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun initView() {
        binding.run {
            vm = viewModel
            musicCompletion()

            ivPlayPause.setOnClickListener {
                setMusicStartOrPause()
            }
            llLyrics.setOnClickListener {
                moveFragment(LyricsFragment.newInstance(), R.id.fv_container)
            }

        }
    }

    override fun initViewModel() {
        viewModel.run {
            getSong()
            songVo.observe(viewLifecycleOwner, EventObserver {
                if (mediaPlayer?.isPlaying == false) {
                    initMediaPlayer(it)
                }
            })
            currentPosition.observe(viewLifecycleOwner, EventObserver {
                binding.sbDuration.progress = it
                setCurrentLyrics(it)
                isMove.value?.peekContent()?.let { isMove ->
                    if (isMove) {
                        this.isMove.value = Event(false)
                        moveSeek(it)
                        mediaPlayer?.seekTo(it)
                    }
                }
            })
        }
    }

    private fun FragmentMusicPlayBinding.musicCompletion() =
        mediaPlayer?.setOnCompletionListener {
            job.cancel()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()
            viewModel.getSong()
            ivPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_60)
        }

    private fun FragmentMusicPlayBinding.setMusicStartOrPause() {
        mediaPlayer?.let { mediaPlayer ->
            if (mediaPlayer.isPlaying) {
                ivPlayPause.setImageResource(R.drawable.ic_baseline_play_arrow_60)
                mediaPlayer.pause()
            } else {
                ivPlayPause.setImageResource(R.drawable.ic_baseline_pause_60)
                sbDuration.max = mediaPlayer.duration
                mediaPlayer.start()
            }
        }
    }

    private fun initMediaPlayer(songVo: SongVo) {
        mediaPlayer?.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(songVo.file)
            prepareAsync()
        }
        playMedia()
    }

    private fun playMedia() {
        job = Job()
        lifecycleScope.launch(scope) {
            while (isActive) {
                binding.run {
                    viewModel.run {
                        mediaPlayer?.let { mediaPlayer ->
                            setCurrentPosition(mediaPlayer.currentPosition)
                            sbDuration.setOnSeekBarChangeListener(object :
                                SeekBar.OnSeekBarChangeListener {
                                override fun onStopTrackingTouch(seekBar: SeekBar) {}
                                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                                override fun onProgressChanged(
                                    seekBar: SeekBar,
                                    progress: Int,
                                    fromUser: Boolean
                                ) {
                                    if (fromUser) {
                                        moveSeek(progress)
                                        mediaPlayer.seekTo(progress)
                                    }
                                }
                            })
                            delay(10L)
                        }
                    }
                }
            }
        }
    }
}