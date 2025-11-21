package com.onerainbow.module.musicplayer.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onerainbow.module.musicplayer.databinding.ItemPlayerlistSongBinding
import com.onerainbow.module.musicplayer.domain.Song
import com.onerainbow.module.musicplayer.service.MusicManager

/**
 * description ： 歌曲播放列表的Adapter
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/17 15:04
 */
class PlayerListAdapter(
    private val onItemClick: (Int) -> Unit // 点击事件回调
) :ListAdapter<Song, PlayerListAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Song>(){
    override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
        return oldItem == newItem
    }

}) {
    var selectionPosition = -1

    inner class ViewHolder(val binding: ItemPlayerlistSongBinding):RecyclerView.ViewHolder(binding.root){
        private var currentData: Song? = null

        fun bind(data: Song, position: Int){
            currentData = data
            binding.apply {
                playerlistItemName.text = data.name
                playerlistItemCreator.text = data.artists.joinToString(separator = "/") { it.name} //作者用/连接e

                if (position == selectionPosition){
                    playerlistItemName.setTextColor(Color.parseColor("#d81e06"))
                    playerlistItemCreator.setTextColor(Color.parseColor("#d81e06"))
                }else{
                    playerlistItemName.setTextColor(Color.parseColor("#17182C"))
                    playerlistItemCreator.setTextColor(Color.parseColor("#17182C"))
                }


            }
        }
        init {
            initClick()
        }

        private fun initClick() {
            binding.apply {
                root.setOnClickListener {
                    onItemClick(layoutPosition) //回调点击事件
                }
                playerlistDelete.setOnClickListener {
                    MusicManager.removeSongAt(layoutPosition)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemPlayerlistSongBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),position)
    }
}