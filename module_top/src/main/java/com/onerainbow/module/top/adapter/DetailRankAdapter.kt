package com.onerainbow.module.top.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.top.R
import com.onerainbow.module.top.bean.DetailRank
import com.onerainbow.module.top.bean.toPlaylists
import com.onerainbow.module.top.databinding.ItemRankBinding
import com.therouter.TheRouter

/**
 * description ： 精选榜的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 16:27
 */
class DetailRankAdapter(val context: Context) :
    ListAdapter<DetailRank, DetailRankAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<DetailRank>() {
        override fun areItemsTheSame(oldItem: DetailRank, newItem: DetailRank): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DetailRank, newItem: DetailRank): Boolean {
            return oldItem == newItem
        }

    }) {
    //图片加载配置
    val requestOptions: RequestOptions =
        RequestOptions().placeholder(R.drawable.loading)
            .fallback(R.drawable.loading)

    inner class ViewHolder(binding: ItemRankBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: DetailRank? = null
        private val cover = binding.imgCover
        private val item = binding.root

        init {
            initClick()
        }

        fun bind(data: DetailRank) {
            currentData = data
            Glide.with(context).load(data.coverImgUrl).apply(requestOptions).into(cover)
        }

        private fun initClick() {
            item.setOnClickListener {
                val playlist = currentData?.toPlaylists()
                TheRouter.build(RoutePath.PLAYLIST)
                    .withParcelable("playlists", playlist)
                    .navigation()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRankBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}