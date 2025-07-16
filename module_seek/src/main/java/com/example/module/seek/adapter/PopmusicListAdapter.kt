package com.example.module.seek.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.module.seek.data.Playlist
import com.example.module.seek.data.PopmusicData
import com.example.module.seek.databinding.ItemHotBinding

/**
 * description ： viewpager2适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/16 17:24
 */
class PopmusicListAdapter(private val context: Context) :
    ListAdapter<Playlist, PopmusicListAdapter.PopmusicviewHolder>(object :DiffUtil.ItemCallback<Playlist>(){
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

        var adapter=PopmusicAdapter{
                item ->
            // 点击事件逻辑
            Toast.makeText(context, "点击了 ${item.name} id${item.id}", Toast.LENGTH_SHORT).show()
        }
        recycleview.layoutManager=LinearLayoutManager(context)
        LinearLayoutManager(context).isSmoothScrollbarEnabled = false // 关闭平滑滚动效果（可选）
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
