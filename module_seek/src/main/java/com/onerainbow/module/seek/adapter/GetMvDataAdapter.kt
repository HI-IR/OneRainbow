package com.onerainbow.module.seek.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.seek.data.Mv
import com.onerainbow.module.seek.databinding.ItemMvBinding
import com.therouter.TheRouter

/**
 * description ： 获取视频相关信息
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 17:07
 */
class GetMvDataAdapter : ListAdapter<Mv, GetMvDataAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val binding: ItemMvBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: Mv? = null

        init {
            initClick()
        }

        fun initClick() {

            binding.root.setOnClickListener {
                currentData?.let {
                    TheRouter.build(RoutePath.MVS)
                        .withLong("id", it.id.toLong())
                        .withString("name", it.name)
                        .navigation()
                }
            }

        }

        fun bind(item: Mv) {
            currentData = item
            binding.myNumber.text = "播放${item.playCount}次"
            binding.mvTitle.text = "${item.name}"
            binding.mvTime.text = "${item.duration.toMinuteSecond()},by"
            Glide.with(binding.mvImg.context)
                .load(item.cover)
                .transform(RoundedCorners(20))
                .into(binding.mvImg)
            // 拼接歌手名
            val allNames = item.artists.joinToString(separator = "、") { it.name }
            val maxSingerLen = 8
            val displaySingers = if (allNames.length > maxSingerLen) {
                allNames.substring(0, maxSingerLen) + "…"
            } else {
                allNames
            }


            // 清空后添加一个 TextView
            val flexSingers = binding.flexMvAuthor
            flexSingers.removeAllViews()
            val tv = TextView(binding.root.context).apply {
                text = displaySingers
                textSize = 12f
                setTextColor(Color.GRAY)
                setPadding(8, 4, 8, 4)
            }
            flexSingers.addView(tv)

        }

        fun Long.toMinuteSecond(): String {
            if (this == 0L) return "暂无数据"
            val totalSeconds = this / 1000 // 毫秒转秒
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            return String.format("%02d:%02d", minutes, seconds)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Mv>() {
            override fun areItemsTheSame(oldItem: Mv, newItem: Mv): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Mv, newItem: Mv): Boolean {
                return oldItem == newItem
            }

        }
    }
}
