package com.onerainbow.module.seek.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager
import com.onerainbow.module.seek.R
import com.onerainbow.module.musicplayer.domain.Artist
import com.onerainbow.module.seek.data.SongLyric
import com.onerainbow.module.seek.databinding.ItemLyricBinding
import com.onerainbow.module.seek.interfaces.GetImgUrl

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 14:02
 */
class LyricDataAdapter(private val getImgUrl: GetImgUrl) :
    ListAdapter<SongLyric, LyricDataAdapter.ViewHolder>(DiffCallback) {
    private var selectedPosition = RecyclerView.NO_POSITION //初始为-1 也就是没有选中

    inner class ViewHolder(var binding: ItemLyricBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: SongLyric? = null

        init {
            initClick()
        }

        // 点击事件：更新选中状态
        fun initClick() {
            binding.root.setOnClickListener {
                //设置高亮
                val previousPosition = selectedPosition
                selectedPosition = bindingAdapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                currentData?.let { it1 ->
                    val convertedArtists = it1.artists.map {
                        Artist(
                            id = it.id,
                            name = it.name
                        )
                    }
                    getImgUrl.getGetImgUrl(it1.id) { imgUrl ->
                        val song = Song(
                            id = it1.id,
                            name = it1.name,
                            artists = convertedArtists,
                            coverUrl = imgUrl
                        )
                        if (MusicManager.addToPlayerList(song)) {
                            ToastUtils.makeText("添加成功")
                        } else {
                            ToastUtils.makeText("添加失败")
                        }

                    }
                }
            }

        }

        fun bind(item: SongLyric, isSelected: Boolean) {
            currentData = item
            binding.tvLyric.text = item.lyrics.txt
            var isExpanded = false

            binding.tvLyric.maxLines = 3 // 初始显示3行
            binding.tvExpand.setOnClickListener {
                if (isExpanded) {
                    binding.tvLyric.maxLines = 3
                    binding.tvExpand.text = "展开 v" // 切换按钮文字
                } else {
                    binding.tvLyric.maxLines = Int.MAX_VALUE
                    binding.tvExpand.text = "收起 ^"
                }
                isExpanded = !isExpanded
            }

            val maxTitleLen = 10
            val displayTitle = if (item.name.length > maxTitleLen) {
                item.name.substring(0, maxTitleLen) + "…"
            } else {
                item.name
            }
            binding.tvSingleTitle.text = displayTitle

            val maxAlbumLen = 6
            val displayAlbum = if (item.album.name.length > maxAlbumLen) {
                item.album.name.substring(0, maxAlbumLen) + "…"
            } else {
                item.album.name
            }
            binding.tvSingleAblum.text = "-$displayAlbum"

            // 处理歌手名
            val maxSingerLen = 12
            val allNames = item.artists.joinToString(separator = "、") { it.name }
            val displaySingers = if (allNames.length > maxSingerLen) {
                allNames.substring(0, maxSingerLen) + "…"
            } else {
                allNames
            }
            binding.flexSingers.text = displaySingers

            // 根据是否选中设置歌名字体颜色
            binding.tvSingleTitle.setTextColor(
                if (isSelected)
                    binding.root.context.getColor(android.R.color.holo_red_dark) // 选中：红色
                else
                    binding.root.context.getColor(android.R.color.holo_blue_dark) // 未选中：黑色
            )
            // 根据是否选中切换图片
            if (isSelected) {
                binding.singleImg.setImageResource(R.drawable.single_open) // 替换成播放中的图片
            } else {
                binding.singleImg.setImageResource(R.drawable.sungle_close) // 恢复成默认图片
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLyricBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = (position == selectedPosition)
        holder.bind(getItem(position), isSelected)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SongLyric>() {
            override fun areItemsTheSame(oldItem: SongLyric, newItem: SongLyric): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SongLyric, newItem: SongLyric): Boolean {
                return oldItem == newItem
            }

        }
    }
}