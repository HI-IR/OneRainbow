package com.onerainbow.page.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.page.musicplayer.api.IMusicplayerService
import com.onerainbow.page.musicplayer.api.bean.Artist
import com.onerainbow.page.musicplayer.api.bean.Song
import com.onerainbow.page.search.R
import com.onerainbow.page.search.data.SongData
import com.onerainbow.page.search.databinding.ItemPlaylistSongBinding
import com.therouter.TheRouter

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 14:54
 */
class SongerAdapter : ListAdapter<SongData, SongerAdapter.ViewHolder>(DiffCallback) {
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(var binding: ItemPlaylistSongBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData : SongData? = null

        init {
            initClick()
        }
        fun initClick(){
            currentData?.let { it1 ->
                val convertedArtists = it1.ar.map {
                    Artist(
                        id = it.id,
                        name = it.name
                    )
                }
                // 点击事件：更新选中状态
                binding.root.setOnClickListener {
                    val previousPosition = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previousPosition) // 刷新之前选中的
                    notifyItemChanged(selectedPosition) // 刷新当前选中的

                    val song = Song(
                        id = it1.id,
                        name = it1.name,
                        artists = convertedArtists,
                        coverUrl = it1.al.picUrl
                    )

                    val isSuccess = TheRouter
                        .get(IMusicplayerService::class.java)
                        ?.addToPlayerList(song)
                        ?: false

                    if (isSuccess) {
                        ToastUtils.makeText("添加成功")
                    } else {
                        ToastUtils.makeText("添加失败")
                    }
                }
            }
        }
        fun bind(item: SongData, isSelected: Boolean) {
            binding.tvSingleAblum.text = "-${item.al.name}"
            binding.tvSingleTitle.text = item.name
            Glide.with(binding.songImg.context)
                .load(item.al.picUrl?:R.drawable.ic_avatar_placeholder)
                .transform(RoundedCorners(20))
                .into(binding.songImg)

            val maxSingerLen = 12
            val allNames = item.ar.joinToString(separator = "、") { it.name }
            val displaySingers = if (allNames.length > maxSingerLen) {
                allNames.substring(0, maxSingerLen) + "…"
            } else {
                allNames
            }
            binding.flexSingers.text = displaySingers

// 设置歌名颜色
            binding.tvSingleTitle.setTextColor(
                if (isSelected)
                    binding.root.context.getColor(android.R.color.holo_red_dark)
                else
                    binding.root.context.getColor(android.R.color.black)
            )


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlaylistSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = (position == selectedPosition)
        holder.bind(getItem(position), isSelected)

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
