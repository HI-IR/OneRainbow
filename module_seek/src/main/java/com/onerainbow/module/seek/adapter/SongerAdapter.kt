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
import com.onerainbow.module.seek.R
import com.onerainbow.module.seek.data.SongData
import com.onerainbow.module.seek.databinding.ItemPlaylistSongBinding
import com.onerainbow.module.musicplayer.model.Song
import com.onerainbow.module.musicplayer.service.MusicManager

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/19 14:54
 */
class SongerAdapter : ListAdapter<SongData, SongerAdapter.ViewHolder>(DiffCallback) {
    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(var binding: ItemPlaylistSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SongData, isSelected: Boolean) {
            binding.tvSingleAblum.text = "-${item.al.name}"
            binding.tvSingleTitle.text = item.name
            Glide.with(binding.songImg.context)
                .load(item.al.picUrl?:R.drawable.ic_avatar_placeholder)
                .transform(RoundedCorners(20))
                .into(binding.songImg)

            item.ar.forEach {
                val maxSingerLen = 12
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
                // 根据是否选中设置歌名字体颜色
                binding.tvSingleTitle.setTextColor(
                    if (isSelected)
                        binding.root.context.getColor(android.R.color.holo_red_dark) // 选中：红色
                    else
                        binding.root.context.getColor(android.R.color.black) // 未选中：黑色
                )
                val convertedArtists = item.ar.map {
                    com.onerainbow.module.musicplayer.model.Artist(
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
                        id = item.id,
                        name = item.name,
                        artists = convertedArtists,
                        coverUrl = item.al.picUrl
                    )
                    Log.d("SongDatail",song.toString())
                    if (MusicManager.addToPlayerList(song)){
                        ToastUtils.makeText("添加成功")
                    }else{
                        ToastUtils.makeText("添加失败")
                    }
                }

            }

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
