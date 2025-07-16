package com.onerainbow.module.recommend.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onerainbow.module.recommend.R
import com.onerainbow.module.recommend.bean.Playlists
import com.onerainbow.module.recommend.databinding.ItemCommunityPicksBinding

/**
 * description ： 网友推荐的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/15 12:23
 */

class CommunityPicksAdapter(private val context: Context) :ListAdapter<List<Playlists>,CommunityPicksAdapter.ViewHolder>(object : DiffUtil.ItemCallback<List<Playlists>>(){
    override fun areItemsTheSame(oldItem: List<Playlists>, newItem: List<Playlists>): Boolean {
        return oldItem.first().id == newItem.first().id
    }

    override fun areContentsTheSame(oldItem: List<Playlists>, newItem: List<Playlists>): Boolean {
        if (oldItem.size != newItem.size) return false
        for (i in oldItem.indices) {
            if (oldItem[i] != newItem[i]) return false
        }
        return true
    }
}){

    inner class ViewHolder(binding:ItemCommunityPicksBinding):RecyclerView.ViewHolder(binding.root){
        private var currentData : List<Playlists>? = null

        val titles = listOf(
            binding.tvTitle1,
            binding.tvTitle2,
            binding.tvTitle3,
        )

        val descs = listOf(
            binding.tvDesc1,
            binding.tvDesc2,
            binding.tvDesc3,
        )

        val covers = listOf(
            binding.imgCover1,
            binding.imgCover2,
            binding.imgCover3,
        )
        val creators = listOf(
            binding.tvCreator1,
            binding.tvCreator2,
            binding.tvCreator3,
        )

        val plays = listOf(
            binding.btnPlay1,
            binding.btnPlay2,
            binding.btnPlay3,
        )

        val items = listOf(
            binding.item1,
            binding.item2,
            binding.item3,
        )
        init {
            initClick()
        }

        private fun initClick() {
            items.forEachIndexed { index, item ->
                item.setOnClickListener {
                    Toast.makeText(context,"点击了歌单,歌单ID ${currentData?.getOrNull(index)?.id}",Toast.LENGTH_SHORT).show()

                }

            }
            plays.forEachIndexed { index, play ->
                play.setOnClickListener {
                    Toast.makeText(context,"点击了歌单播放,歌单ID ${currentData?.getOrNull(index)?.id}",Toast.LENGTH_SHORT).show()
                }

            }
        }

        fun bind(data : List<Playlists>){
            currentData = data
            val requestOptions: RequestOptions = RequestOptions().placeholder(R.drawable.loading)
                .fallback(R.drawable.loading)
            //按照Index分别绑定视图数据
            data.forEachIndexed { index, playlists ->
                titles[index].text = playlists.name
                descs[index].text = playlists.description
                //加载网络图片
                Glide.with(context).load(playlists.coverImgUrl).apply(requestOptions).into(covers[index])

                creators[index].text = "-${playlists.creator.nickname}"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCommunityPicksBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}