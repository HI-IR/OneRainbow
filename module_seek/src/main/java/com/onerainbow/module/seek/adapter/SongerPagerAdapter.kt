package com.onerainbow.module.seek.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.module.musicplayer.domain.Artist
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.seek.R
import com.onerainbow.module.seek.data.SongData
import com.onerainbow.module.seek.databinding.ItemPageBinding
import com.onerainbow.module.seek.databinding.ItemPlaylistSongBinding

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/22 21:34
 */

class SongPageAdapter : ListAdapter<SongData, SongPageAdapter.PageViewHolder>(DiffCallback) {

    private var selectedPosition = RecyclerView.NO_POSITION
    inner class PageViewHolder(val binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentStartIndex = 0

        init {
            val containers = listOf(binding.song1, binding.song2, binding.song3)
            containers.forEachIndexed { index, songBinding ->
                songBinding.root.setOnClickListener {
                    val clickedPosition = currentStartIndex + index
                    if (clickedPosition != selectedPosition) {
                        val previousPosition = selectedPosition
                        selectedPosition = clickedPosition
                        notifyItemChanged(previousPosition / 3)
                        notifyItemChanged(selectedPosition / 3)
                        // 触发播放逻辑，可通过接口或直接写调用
                        val item = currentList.getOrNull(clickedPosition)
                        item?.let {
                            val convertedArtists = it.ar.map { artist ->
                                Artist(id = artist.id, name = artist.name)
                            }
                            val song = Song(it.id, it.name, convertedArtists, it.al.picUrl)
                            Log.d("SongDetail", song.toString())
                            if (MusicManager.addToPlayerList(song)) {
                                ToastUtils.makeText("添加成功")
                            } else {
                                ToastUtils.makeText("添加失败")
                            }
                        }
                    }
                }
            }
        }

        fun bindSongData(binding: ItemPlaylistSongBinding, item: SongData, isSelected: Boolean) {
            binding.tvSingleAblum.text = "-${item.al.name}"
            binding.tvSingleTitle.text = item.name

            Glide.with(binding.songImg.context)
                .load(item.al.picUrl)
                .transform(RoundedCorners(20))
                .into(binding.songImg)

            val maxSingerLen = 12//设置最多字符
            val allNames = item.ar.joinToString(separator = "、") { it.name }
            val displaySingers = if (allNames.length > maxSingerLen) {
                allNames.substring(0, maxSingerLen) + "…"
            } else {
                allNames
            }

            val flexSingers = binding.flexSingers
            flexSingers.removeAllViews()
            val tv = TextView(binding.root.context).apply {
                text = displaySingers
                textSize = 12f
                setTextColor(Color.GRAY)
                setPadding(8, 4, 8, 4)
            }
            flexSingers.addView(tv)

            // 高亮选中项
            binding.tvSingleTitle.setTextColor(
                if (isSelected)
                    binding.root.context.getColor(android.R.color.holo_red_dark)
                else
                    binding.root.context.getColor(android.R.color.black)
            )
        }

            fun bindItemData(position: Int) {
                currentStartIndex = position * 3
                val endIndex = minOf(currentStartIndex + 3, currentList.size)
                val pageData = currentList.subList(currentStartIndex, endIndex)

                val containers = listOf(binding.song1, binding.song2, binding.song3)
                containers.forEachIndexed { index, songBinding ->
                    if (index < pageData.size) {
                        val item = pageData[index]
                        bindSongData(songBinding, item, (currentStartIndex + index) == selectedPosition)
                        songBinding.root.visibility = View.VISIBLE
                    } else {
                        songBinding.root.visibility = View.INVISIBLE
                    }
                }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding = ItemPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // 每页3条 向上取整
        return (currentList.size + 2) / 3
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bindItemData(position)
    }


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SongData>() {
            override fun areItemsTheSame(oldItem: SongData, newItem: SongData): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SongData, newItem: SongData): Boolean {
                return oldItem == newItem
            }
        }
    }
}

