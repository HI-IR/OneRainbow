package com.onerainbow.module.seek.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.lib.route.RoutePath
import com.onerainbow.module.seek.data.Playlist
import com.onerainbow.module.seek.databinding.ItemHotBinding
import com.therouter.TheRouter

/**
 * description ： viewpager2适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/16 17:24
 */
class PopmusicListAdapter(private val context: Context) : ListAdapter<Playlist, PopmusicListAdapter.PopmusicviewHolder>(object :DiffUtil.ItemCallback<Playlist>(){
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.tracks== newItem.tracks
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem ==newItem
    }
}) {
    inner class PopmusicviewHolder(binding:ItemHotBinding):RecyclerView.ViewHolder(binding.root){
       private var popmusicData:Playlist? =null
        private var recycleview:RecyclerView =binding.recycleviewHotOne
        private var tv_tilie =binding.tvHotTitleOne


     fun bind(data:Playlist){
        popmusicData =data
        tv_tilie.text = data.name

        // 2. 限制 tracks 只取前 20 条
        val limitedTracks = data.tracks.take(20)

        val adapter=PopmusicAdapter{ item ->
            // 点击事件逻辑
            TheRouter.build(RoutePath.FINISHSEEK)
                .withString("keyword", item.name)
                .navigation()
        }
        recycleview.layoutManager=LinearLayoutManager(context)
        LinearLayoutManager(context).isSmoothScrollbarEnabled = false
        recycleview.adapter=adapter
        adapter.submitList(limitedTracks)
    }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopmusicListAdapter.PopmusicviewHolder {
        return PopmusicviewHolder(ItemHotBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: PopmusicviewHolder, position: Int) {
        holder.bind(getItem(position))

    }



}
