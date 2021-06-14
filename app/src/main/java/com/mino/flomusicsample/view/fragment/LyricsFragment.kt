package com.mino.flomusicsample.view.fragment

import androidx.fragment.app.activityViewModels
import com.mino.flomusicsample.R
import com.mino.flomusicsample.base.BaseFragment
import com.mino.flomusicsample.databinding.FragmentLyricsBinding
import com.mino.flomusicsample.util.EventObserver
import com.mino.flomusicsample.util.toLyricsVoList
import com.mino.flomusicsample.view.adapter.LyricsAdapter
import com.mino.flomusicsample.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LyricsFragment : BaseFragment<FragmentLyricsBinding>(R.layout.fragment_lyrics) {

    private val lyricsAdapter: LyricsAdapter by lazy {
        LyricsAdapter({ clickPosition ->
            adapterClickAction(clickPosition)
        }, { currentPosition ->
            setScroll(currentPosition)
        }).apply {
            setHasStableIds(true)
        }
    }
    private val viewModel: MainViewModel by activityViewModels()

    override fun initView() {
        binding.run {
            rvContent.adapter = lyricsAdapter
            ivClose.setOnClickListener {
                onBackPressed()
            }
        }
    }

    override fun initViewModel() {
        viewModel.run {
            lyricsList.observe(viewLifecycleOwner, EventObserver {
                lyricsAdapter.setItemsDiff(it.toLyricsVoList())
            })

            lyricsTimeFirst.observe(viewLifecycleOwner, EventObserver { lyricsTimeFirst ->
                lyricsTimeList.value?.peekContent()?.let { lyricsTimeList ->
                    lyricsList.value?.peekContent()?.let {
                        lyricsAdapter.setIndex(
                            lyricsTimeList.indexOf(lyricsTimeFirst),
                            it.toLyricsVoList()
                        )
                    }
                }
            })
        }
    }

    private fun setScroll(index: Int) {
        binding.rvContent.scrollToPosition(index)
    }

    private fun adapterClickAction(index: Int) {
        if (binding.tbMove.isChecked) {
            viewModel.run {
                lyricsTimeList.value?.peekContent()?.let {
                    setCurrentPosition(it[index], true)
                }
            }
        } else {
            onBackPressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LyricsFragment()
    }
}