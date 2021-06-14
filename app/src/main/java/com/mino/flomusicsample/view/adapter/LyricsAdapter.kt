package com.mino.flomusicsample.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mino.flomusicsample.util.DiffDefault
import com.mino.flomusicsample.R
import com.mino.flomusicsample.databinding.ItemLyricsBinding
import com.mino.flomusicsample.vo.LyricsVo

class LyricsAdapter(private val onClickAction: (Int) -> Unit, private val onScrollMove : (Int) -> Unit) : RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder>() {

    private val lyricsList = mutableListOf<LyricsVo>()
    private var current = 0

    fun setItemsDiff(items: List<LyricsVo>) {
        calDiff(items)
        lyricsList.clear()
        lyricsList.addAll(items)
    }

    fun setIndex(index: Int, item: List<LyricsVo>) {
        item[current].isCurrent = false
        item[index].isCurrent = true
        current = index
        calDiff(item)
        Log.e("SAdasdas",item.toString())
    }

    private fun calDiff(updateList: List<LyricsVo>) {
        val diff = DiffDefault(lyricsList, updateList)
        val diffResult = DiffUtil.calculateDiff(diff)
        lyricsList.clear()
        lyricsList.addAll(updateList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int = lyricsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricsViewHolder {
        val binding = DataBindingUtil.inflate<ItemLyricsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_lyrics,
            parent, false
        )
        return LyricsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LyricsViewHolder, position: Int) {
        holder.bind(lyricsList[position])
        if(lyricsList[position].isCurrent){
            onScrollMove(position)
        }
    }

    inner class LyricsViewHolder(private val binding: ItemLyricsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClickAction(adapterPosition)
            }
        }
        fun bind(lyricsVo: LyricsVo) {
            binding.item = lyricsVo
        }
    }
}