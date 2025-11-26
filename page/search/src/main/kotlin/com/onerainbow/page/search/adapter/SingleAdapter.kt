package com.onerainbow.page.search.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.lib.base.utils.ToastUtils
import com.onerainbow.page.musicplayer.api.IMusicplayerService
import com.onerainbow.page.musicplayer.api.bean.Artist
import com.onerainbow.page.musicplayer.api.bean.Song
import com.onerainbow.page.search.R
import com.onerainbow.page.search.data.Songi
import com.onerainbow.page.search.databinding.ItemSingleBinding
import com.onerainbow.page.search.interfaces.GetImgUrl
import com.therouter.TheRouter

/**
 * description ： 单曲列表适配器
 * author : summer_palace2
 * email : 2992203079@qq.com
 * date : 2025/7/17 15:45
 */
class SingleAdapter(private val getImgUrl: GetImgUrl) :
    ListAdapter<Songi, SingleAdapter.ViewHolder>(DiffCallback) {

    private var selectedPosition = RecyclerView.NO_POSITION // 当前选中的播放项


    inner class ViewHolder(val binding: ItemSingleBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: Songi? = null

        init {
            initClick()
        }

        fun initClick() {
            // 点击事件：更新选中状态

            binding.root.setOnClickListener {
                currentData?.let { it1 ->
                    val convertedArtists = it1.artists.map {
                        Artist(
                            id = it.id,
                            name = it.name
                        )
                    }
                    val previousPosition = selectedPosition
                    selectedPosition = bindingAdapterPosition  // 这里获取正确的位置
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)
                    Log.d("SingleAdapter", "Clicked: ${it1.name}")
                    getImgUrl.getGetImgUrl(it1.id) { imgUrl ->
                        val song = Song(
                            id = it1.id,
                            name = it1.name,
                            artists = convertedArtists,
                            coverUrl = imgUrl
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
        }

        fun bind(item: Songi, isSelected: Boolean) {
            currentData = item
            binding.tvSingleAblum.text = "-${item.album.name}"

            // 处理歌名显示（截断）
            val maxTitleLen = 8
            val displayTitle = if (item.name.length > maxTitleLen) {
                item.name.substring(0, maxTitleLen) + "…"
            } else {
                item.name
            }
            binding.tvSingleTitle.text = displayTitle

             // 处理专辑名显示（截断）
            val maxAlbumLen = 6
            val displayAlbum = if (item.album.name.length > maxAlbumLen) {
                item.album.name.substring(0, maxAlbumLen) + "…"
            } else {
                item.album.name
            }
            binding.tvSingleAblum.text = "-$displayAlbum"

            // 处理歌手名拼接及显示（截断）
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
        val binding = ItemSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = (position == selectedPosition)
        holder.bind(getItem(position), isSelected)

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Songi>() {
            override fun areItemsTheSame(oldItem: Songi, newItem: Songi): Boolean {
                // 通过 ID 判断是否是同一首歌
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Songi, newItem: Songi): Boolean {
                // 判断内容是否相同
                return oldItem == newItem
            }
        }
    }

}
