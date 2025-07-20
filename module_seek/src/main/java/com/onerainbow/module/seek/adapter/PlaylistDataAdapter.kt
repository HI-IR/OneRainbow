package com.onerainbow.module.seek.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.onerainbow.module.seek.data.Playlists
import com.onerainbow.module.seek.databinding.ItemPlaylistBinding
import com.onerainbow.lib.route.RoutePath
import com.therouter.TheRouter

/**
 * description ： 歌单列表适配器
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 20:15
 */
class PlaylistDataAdapter :ListAdapter<Playlists,PlaylistDataAdapter.ViewHolder>(DiffCallback) {
    inner class ViewHolder(val binding :ItemPlaylistBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:Playlists){
            binding.tvPlaylistTitle.text = item.name
            Glide.with(binding.playlistImg.context)
                .load(item.coverImgUrl)
                .transform(RoundedCorners(20))
                .into(binding.playlistImg)

            binding.tvPlaylistAuthor.text ="by${item.creator.nickname}"
            binding.tvPlaylistNumber.text ="${item.trackCount}首音乐"
            binding.root.setOnClickListener{
                TheRouter.build(RoutePath.PLAYLIST)
                    .withParcelable("playlists",item)
                    .navigation()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))

    }
    companion object{
        private val DiffCallback =object :DiffUtil.ItemCallback<Playlists>(){
            override fun areItemsTheSame(oldItem: Playlists, newItem: Playlists): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Playlists, newItem: Playlists): Boolean {
               return oldItem == newItem
            }

        }
    }

}