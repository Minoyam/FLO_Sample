package com.mino.flomusicsample.view.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
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

    private lateinit var exoPlayer: SimpleExoPlayer
    private lateinit var job: Job

    private val viewModel: MainViewModel by activityViewModels()
    private val scope: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        exoPlayer.release()
    }

    override fun initView() {
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                when (playbackState) {
                    Player.STATE_ENDED -> {
                        viewModel.getSong()
                    }
                    else -> {
                    }
                }
            }
        })
        binding.run {
            vm = viewModel
            exoPlayerControl.player = exoPlayer
            llLyrics.setOnClickListener {
                moveFragment(LyricsFragment.newInstance(), R.id.fv_container)
            }

        }
    }

    override fun initViewModel() {
        viewModel.run {
            getSong()
            songVo.observe(viewLifecycleOwner, EventObserver {
                if (!exoPlayer.isPlaying) {
                    initMediaPlayer(it)
                }
            })
            currentPosition.observe(viewLifecycleOwner, EventObserver {
                setCurrentLyrics(it)
                isMove.value?.peekContent()?.let { isMove ->
                    if (isMove) {
                        this.isMove.value = Event(false)
                        moveSeek(it)
                        exoPlayer.seekTo(it.toLong())
                    }
                }
            })
        }
    }

    private fun initMediaPlayer(songVo: SongVo) {
        val mediaItem = MediaItem.fromUri(songVo.file)
        exoPlayer.apply {
            setMediaItem(mediaItem)
            prepare()
        }
        playMedia()
    }

    private fun playMedia() {
        job = Job()
        lifecycleScope.launch(scope) {
            while (isActive) {
                viewModel.setCurrentPosition(exoPlayer.currentPosition.toInt())
                delay(10L)
            }
        }
    }
}