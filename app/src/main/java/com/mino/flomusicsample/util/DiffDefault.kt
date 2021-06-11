package com.mino.flomusicsample.util

import androidx.recyclerview.widget.DiffUtil
import com.mino.flomusicsample.vo.LyricsVo

class DiffDefault(
    private val oldItems: List<LyricsVo>,
    private val newItems: List<LyricsVo>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int =
        oldItems.size

    override fun getNewListSize(): Int =
        newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem.lyrics == newItem.lyrics
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]

        return oldItem == newItem
    }
}