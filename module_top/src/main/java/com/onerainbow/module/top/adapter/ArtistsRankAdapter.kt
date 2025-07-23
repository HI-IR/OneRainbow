package com.onerainbow.module.top.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.lib.base.anim.scaleAnim
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.top.R
import com.onerainbow.module.top.bean.Artist
import com.onerainbow.module.top.databinding.ItemArtistsBinding
import com.therouter.TheRouter

/**
 * description ： 歌手榜的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 19:47
 */
class ArtistsRankAdapter(private val context: Context) :
    ListAdapter<Artist, ArtistsRankAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Artist, newItem: Artist): Boolean {
            return oldItem == newItem
        }

    }) {
    val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
        .fallback(R.drawable.loading)

    inner class ViewHolder(binding: ItemArtistsBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: Artist? = null
        private val cover = binding.artistsImgCover
        private val score = binding.artistsTvScore
        private val name = binding.artistsTvName
        private val trend = binding.artistsImgTrend
        private val trendCount = binding.artistsTvTrendCount
        private val item = binding.root
        private val rank = binding.artistsTvRank

        init {
            initClick()
        }

        private fun initClick() {
            item.setOnClickListener {
                currentData?.let {
                    TheRouter.build(RoutePath.PLAYLIST).withLong("id", it.id).navigation()
                }
            }
            item.scaleAnim()
        }

        fun bind(data: Artist, index: Int) {
            currentData = data
            Glide.with(context).load(data.picUrl).apply(requestOptions)
                .into(cover)
            score.text = data.score.toString()
            name.text = data.name
            when {
                index < data.lastRank -> {
                    //排名更靠前了
                    trend.setImageResource(R.drawable.rank_up)
                    trendCount.text = "${data.lastRank - index}"
                    trendCount.visibility = View.VISIBLE
                }

                index > data.lastRank -> {
                    //排名靠后了
                    trend.setImageResource(R.drawable.rank_down)
                    trendCount.text = "${data.lastRank - index}"
                    trendCount.visibility = View.VISIBLE
                }

                else -> {
                    //排名没变
                    trend.setImageResource(R.drawable.rank_same)
                    trendCount.visibility = View.INVISIBLE
                }
            }
            if (index in 0..2) {
                rank.apply {
                    //前3名用特殊颜色+字体加大
                    text = "${index + 1}"
                    setTextColor(Color.parseColor("#17182C"))
                    textSize = 18f
                }
            } else {
                rank.apply {
                    text = "${index + 1}"
                    setTextColor(Color.parseColor("#868894"))
                    textSize = 14f
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemArtistsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }
}