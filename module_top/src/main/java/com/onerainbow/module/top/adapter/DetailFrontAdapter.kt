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
import com.onerainbow.module.top.databinding.ItemFrontBinding
import com.therouter.TheRouter

/**
 * description ： 概要Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/20 14:19
 */
class DetailFrontAdapter(val context: Context) :
    ListAdapter<DetailRank, DetailFrontAdapter.ViewHolder>(object :
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


    inner class ViewHolder(binding: ItemFrontBinding) : RecyclerView.ViewHolder(binding.root) {
        private var currentData: DetailRank? = null
        private val title = binding.tvName
        private val cover = binding.imgCover
        private val updateFrequency = binding.tvUpdateFrequency

        //用于来存排行榜的信息，每个list对应一个要填相同信息的View
        private val names = listOf(binding.tvNo1Name, binding.tvNo2Name, binding.tvNo3Name)
        private val creators =
            listOf(binding.tvNo1Creator, binding.tvNo2Creator, binding.tvNo3Creator)

        //点击卡片
        private val item = binding.root

        init {
            initClick()
        }

        private fun initClick() {
            item.setOnClickListener {
                val playlist = currentData?.toPlaylists()
                TheRouter.build(RoutePath.PLAYLIST)
                    .withParcelable("playlists", playlist)
                    .navigation()
            }
        }

        fun bind(data: DetailRank) {
            currentData = data
            //加载图片
            Glide.with(context).load(data.coverImgUrl).apply(requestOptions).into(cover)
            title.text = data.name
            updateFrequency.text = data.updateFrequency
            data.tracks.forEachIndexed { index, detailTrack ->
                names[index].text = detailTrack!!.first
                creators[index].text = detailTrack!!.second
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemFrontBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}