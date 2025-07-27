package com.onerainbow.module.seek.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.module.musicplayer.domain.Artist
import com.onerainbow.module.seek.data.SongGetPlay
import com.onerainbow.module.seek.databinding.ItemPlaylistSongBinding
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 10:31
 */
class SongListDetailAdapter :
    ListAdapter<SongGetPlay, SongListDetailAdapter.ViewHolder>(DiffCallback) {
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(var binding: ItemPlaylistSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var currentData: SongGetPlay? = null

        init {
            initClick()
        }

        fun initClick() {
            binding.root.setOnClickListener {
                currentData?.let {
                    val convertedArtists = it.ar.map {
                        Artist(id = it.id, name = it.name)
                    }
                    val song = Song(
                        id = it.id,
                        name = it.name,
                        artists = convertedArtists,
                        coverUrl = it.al.picUrl
                    )
                    val previousPosition = selectedPosition
                    selectedPosition = bindingAdapterPosition
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)

                    Log.d("SongDetail", song.toString())
                    if (MusicManager.addToPlayerList(song)) {
                        ToastUtils.makeText("添加成功")
                    } else {
                        ToastUtils.makeText("添加失败")
                    }
                }
            }
        }


        fun bind(item: SongGetPlay, isSelected: Boolean) {
            currentData = item
            binding.tvSingleAblum.text =
                item.al.name.takeIf { it.isNotBlank() }?.let { "-$it" } ?: ""
            binding.tvSingleTitle.text = item.name
            Glide.with(binding.songImg.context)
                .load(item.al.picUrl)
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
        private val DiffCallback = object : DiffUtil.ItemCallback<SongGetPlay>() {
            override fun areItemsTheSame(oldItem: SongGetPlay, newItem: SongGetPlay): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SongGetPlay, newItem: SongGetPlay): Boolean {
                return oldItem == newItem
            }

        }

    }
}