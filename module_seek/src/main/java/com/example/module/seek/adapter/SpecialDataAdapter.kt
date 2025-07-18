package com.example.module.seek.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.module.seek.R
import com.example.module.seek.data.AlbumSpecial
import com.example.module.seek.data.SpecialData

import com.example.module.seek.databinding.ItemSpecialBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/18 15:48
 */
class SpecialDataAdapter : ListAdapter<AlbumSpecial, SpecialDataAdapter.ViewHolder>(DiffCallBack) {
    inner class ViewHolder(val binding: ItemSpecialBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AlbumSpecial) {
            binding.tvSpecialTitle.text = item.name
            binding.tvSpecialTime.text = item.publishTime.formatTime("yyyy-MM-dd")
            Glide.with(binding.imgCover1.context)
                .load(item.blurPicUrl)
                .placeholder(R.drawable.ic_avatar_placeholder) // 加载中显示
                .error(R.drawable.ic_avatar_placeholder)       // 加载失败显示
                .fallback(R.drawable.ic_avatar_placeholder)
                .transform(RoundedCorners(20))
                .into(binding.imgCover1)// url 为空时显示
            item.artists.forEach { artists ->
                // 对歌手名拼接超过12字符截断
                val maxSingerLen = 12
                val allNames = item.artists.joinToString(separator = "、") { it.name }
                val displaySingers = if (allNames.length > maxSingerLen) {
                    allNames.substring(0, maxSingerLen) + "…"
                } else {
                    allNames
                }

                val flexSingers = binding.flexSpecialAuthor
                flexSingers.removeAllViews()
                val tv = TextView(binding.root.context).apply {
                    text = displaySingers
                    textSize = 12f
                    setTextColor(Color.GRAY)
                    setPadding(8, 4, 8, 4)
                }
                flexSingers.addView(tv)
            }

        }

        fun Long.formatTime(pattern: String = "yyyy-MM-dd HH:mm"): String {
            val sdf = SimpleDateFormat(pattern)
            sdf.timeZone = TimeZone.getTimeZone("Asia/Shanghai") // 设置时区为北京时间
            return sdf.format(Date(this))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpecialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<AlbumSpecial>() {

            override fun areItemsTheSame(oldItem: AlbumSpecial, newItem: AlbumSpecial): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: AlbumSpecial, newItem: AlbumSpecial): Boolean {
                return oldItem == newItem
            }

        }

    }
}